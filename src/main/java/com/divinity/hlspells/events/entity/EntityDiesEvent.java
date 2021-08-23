package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.ModTotemItem;
import com.divinity.hlspells.items.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.items.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.setup.client.ClientSetup;
import com.divinity.hlspells.util.CuriosCompat;
import com.divinity.hlspells.util.Util;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDiesEvent {

    //Prioritize Death totem savers
    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            World world = player.level;

            //Responsible for stopping the totem in both hands being activated
            boolean griefingTotem = true;
            boolean escapingTotem = true;
            boolean returnTotem = true;
            boolean keepingTotem = true;

            if (ModList.get().isLoaded("curios")) {
                if (CuriosCompat.getItemInCuriosSlot(player, Items.TOTEM_OF_UNDYING).isPresent()) {
                    escapingTotem = false;
                }

                if (CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_KEEPING.get()).isPresent()) {
                    CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(map -> {
                        ItemStack stack = map.getRight();
                        stack.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                            cap.hasDied(true);
                            cap.setTotemInHand(Hand.MAIN_HAND);
                            cap.setInventoryNBT(player.inventory.save(new ListNBT()));
                        });
                    });
                    keepingTotem = false;
                    griefingTotem = false;
                }


                if (CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_GRIEFING.get()).isPresent()) {
                    CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_GRIEFING.get()).ifPresent(map -> {
                        ItemStack stack = map.getRight();
                        world.explode(player, player.getX(), player.getY(), player.getZ(), 5.0F, Explosion.Mode.BREAK);
                        //TODO destroy the item in the curio slot
                    });
                    griefingTotem = false;
                }

                if (CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_ESCAPING.get()).isPresent()) {
                    CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_ESCAPING.get()).ifPresent(map -> {
                        ItemStack stack = map.getRight();
                        ModTotemItem.vanillaTotemBehavior(player, stack, ItemInit.TOTEM_OF_ESCAPING.get());
                    });
                    player.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                    // Teleport the player randomly nearby
                    for (int i = 0; i < 16; ++i) {
                        double xRand = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = MathHelper.clamp(player.getY() + (player.getRandom().nextInt(16) - 8), 0.0D, world.getHeight() - 1D);
                        double zRand = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        player.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                    escapingTotem = false;
                    returnTotem = false;
                }

                if (CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_RETURNING.get()).isPresent()) {
                    CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_RETURNING.get()).ifPresent(map -> {
                        ItemStack stack = map.getRight();
                        stack.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                            cap.hasDied(true);
                            cap.setTotemInHand(Hand.MAIN_HAND);
                        });
                    });
                    returnTotem = false;
                }
            }
            for (Hand hand : Hand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);
                if (heldItem.getItem() == Items.TOTEM_OF_UNDYING) {
                    escapingTotem = false;
                }

                // TOTEM OF KEEPING (Saves player inventory and updates the totem the player has died)
                if (heldItem.getItem() == ItemInit.TOTEM_OF_KEEPING.get() && keepingTotem) {
                    heldItem.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                        cap.hasDied(true);
                        if (hand == Hand.MAIN_HAND) cap.setTotemInHand(Hand.MAIN_HAND);
                        else if (hand == Hand.OFF_HAND) cap.setTotemInHand(Hand.OFF_HAND);
                        cap.setInventoryNBT(player.inventory.save(new ListNBT()));
                    });
                    keepingTotem = false;
                    griefingTotem = false;
                }

                // TOTEM OF GRIEFING (Explodes if the totem is held)
                if (heldItem.getItem() == ItemInit.TOTEM_OF_GRIEFING.get() && griefingTotem) {

                    world.explode(player, player.getX(), player.getY(), player.getZ(), 5.0F, Explosion.Mode.BREAK);
                    // Removes the totem from dropping
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    griefingTotem = false;
                }

                // TOTEM OF ESCAPING (Does vanilla totem logic and teleports the player randomly)
                if (heldItem.getItem() == ItemInit.TOTEM_OF_ESCAPING.get() && escapingTotem) {
                    event.setCanceled(true);
                    ModTotemItem.vanillaTotemBehavior(player, heldItem, ItemInit.TOTEM_OF_ESCAPING.get());
                    player.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                    // Teleport the player randomly nearby
                    for (int i = 0; i < 16; ++i) {
                        double xRand = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = MathHelper.clamp(player.getY() + (player.getRandom().nextInt(16) - 8), 0.0D, world.getHeight() - 1D);
                        double zRand = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        player.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                    escapingTotem = false;
                    returnTotem = false;
                }

                // TOTEM OF RETURNING (Saves the hand the totem is in and updates the totem the player has died)
                if (heldItem.getItem() == ItemInit.TOTEM_OF_RETURNING.get() && returnTotem) {
                    heldItem.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                        cap.hasDied(true);
                        if (hand == Hand.MAIN_HAND) cap.setTotemInHand(Hand.MAIN_HAND);
                        else if (hand == Hand.OFF_HAND) cap.setTotemInHand(Hand.OFF_HAND);
                    });
                    returnTotem = false;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            // If true the inventory is loaded back and drops are removed
            boolean[] keepingTotem = new boolean[1];
            for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
                ItemStack stack = itemEntityIterator.next().getItem();
                // TOTEM OF RETURNING (Sets BlockPos to teleport to and sets the hand the totem should be in)
                if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap ->
                    {
                        cap.setBlockPos(player.blockPosition());
                        Hand hand = cap.getTotemInHand();
                        // The drop is removed here to avoid deleting both totems held in both hands
                        if (hand == Hand.MAIN_HAND) {
                            player.inventory.add(player.inventory.selected, stack);
                            itemEntityIterator.remove();
                            cap.setTotemInHand(null);
                        } else if (hand == Hand.OFF_HAND) {
                            player.inventory.offhand.set(0, stack);
                            itemEntityIterator.remove();
                            cap.setTotemInHand(null);
                        }
                    });
                }
                // TOTEM OF KEEPING (Reloads player inventory even after dying and disables inventory from spilling)
                if (stack.getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                        Hand hand = cap.getTotemInHand();
                        if (hand == Hand.MAIN_HAND || hand == Hand.OFF_HAND) {
                            player.inventory.load(cap.getInventoryNBT());
                            cap.setTotemInHand(null);
                            keepingTotem[0] = true;
                        }
                    });
                }
            }
            if (keepingTotem[0]) {
                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public static void onEntityCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath() && !event.getEntity().level.isClientSide()) {
            PlayerEntity original = event.getOriginal();
            PlayerEntity current = event.getPlayer();
            // TOTEM OF RETURNING (Adds totem to the inventory)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.setItem(original.inventory.selected, original.inventory.getSelected());
            } else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.offhand.set(0, original.inventory.offhand.get(0));
            }

            // TOTEM OF KEEPING (Restores the inventory)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                int mainSlot = original.inventory.findSlotMatchingItem(original.getMainHandItem());
                original.inventory.getItem(mainSlot != -1 ? mainSlot : 0).shrink(mainSlot != -1 ? 1 : 0);
                current.inventory.replaceWith(original.inventory);
                displayActivation(current, ItemInit.TOTEM_OF_KEEPING.get(), true);
            } else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                original.inventory.offhand.get(0).shrink(1);
                current.inventory.replaceWith(original.inventory);
                displayActivation(current, ItemInit.TOTEM_OF_KEEPING.get(), true);
            }
        }
    }

    // TOTEM OF RETURNING (Teleports the player to last died pos when right clicked)
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getPlayer() != null) {
            PlayerEntity player = event.getPlayer();
            World world = player.level;
            if (!world.isClientSide()) {
                for (Hand hand : Hand.values()) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                        stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                            BlockPos pos = cap.getBlockPos();
                            displayActivation(player, ItemInit.TOTEM_OF_RETURNING.get(), true);
                            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 0.3F, 0.3F);
                            player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                            player.setItemInHand(hand, ItemStack.EMPTY);
                            Util.teleportParticles(world, pos, 200);
                            world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.7F, 0.7F);
                        });
                        return;
                    }
                }
            }
        }
    }

    /**
     * Method to hide the client side call to show totem activation and/or particles
     */
    public static void displayActivation(PlayerEntity playerEntity, Item item, boolean particleIn) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientSetup.displayActivation(playerEntity, new ItemStack(item), particleIn));
    }
}
