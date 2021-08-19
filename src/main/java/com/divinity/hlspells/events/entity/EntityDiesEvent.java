package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.ModTotemItem;
import com.divinity.hlspells.items.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.items.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDiesEvent {

    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            World world = player.level;
            boolean returnTotem = true;
            for (Hand hand : Hand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);
                // TOTEM OF GRIEFING
                if (heldItem.getItem() == ItemInit.TOTEM_OF_GRIEFING.get()) {
                    world.explode(player, player.getX(), player.getY(), player.getZ(), 5.0F, Explosion.Mode.BREAK);
                }

                // TOTEM OF ESCAPING
                if (heldItem.getItem() == ItemInit.TOTEM_OF_ESCAPING.get()) {
                    ModTotemItem.vanillaTotemBehavior(event, player, heldItem, ItemInit.TOTEM_OF_ESCAPING.get());
                    player.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                    // teleport the player randomly nearby
                    for (int i = 0; i < 16; ++i) {
                        double xRand = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = MathHelper.clamp(player.getY() + (player.getRandom().nextInt(16) - 8), 0.0D, world.getHeight() - 1D);
                        double zRand = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        player.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                }

                if (heldItem.getItem() == ItemInit.TOTEM_OF_RETURNING.get() && returnTotem) {
                    heldItem.getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                        cap.hasDied(true);
                        if (hand == Hand.MAIN_HAND)
                            cap.setTotemInHand(Hand.MAIN_HAND);
                        else if (hand == Hand.OFF_HAND)
                            cap.setTotemInHand(Hand.OFF_HAND);
                    });
                    returnTotem = false;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) event.getEntity();

        // TOTEM OF RETURNING
        for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
            ItemStack stack = itemEntityIterator.next().getItem();
            if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap ->
                {
                    itemEntityIterator.remove();
                    cap.setXPos(player.getX());
                    cap.setYPos(player.getY());
                    cap.setZPos(player.getZ());
                    Hand hand = cap.getTotemInHand();
                    if (hand == Hand.MAIN_HAND)
                        player.inventory.add(player.inventory.selected, stack);
                    else if (hand == Hand.OFF_HAND)
                        player.inventory.offhand.set(0, stack);
                });
            }
        }
    }

    // TOTEM OF RETURNING
    @SubscribeEvent
    public static void onEntityCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            PlayerEntity original = event.getOriginal();
            PlayerEntity current = event.getPlayer();
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.setItem(original.inventory.selected, original.inventory.getSelected());
            }

            if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.offhand.set(0, original.inventory.offhand.get(0));
            }
        }
    }

    // TOTEM OF RETURNING
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getPlayer() != null) {
            PlayerEntity player = event.getPlayer();
            World world = player.level;

            if (!player.level.isClientSide()) {
                for (Hand hand : Hand.values()) {
                    if (player.getItemInHand(hand).getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                        player.getItemInHand(hand).getCapability(TotemItemProvider.TOTEM_CAP, null).filter(ITotemCap::getHasDied).ifPresent(cap ->
                        {
                            if (cap.getTotemInHand() == hand) {
                                player.teleportTo(cap.getXPos(), cap.getYPos(), cap.getZPos());
                                cap.hasDied(false);
                                Util.teleportParticles(world, new BlockPos(cap.getXPos(), cap.getYPos(), cap.getZPos()), 200);
                                player.getItemInHand(hand).shrink(1);
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 0.3F, 0.3F);
                                Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(ItemInit.TOTEM_OF_RETURNING.get()));
                                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.7F, 0.7F);
                            }
                        });
                    }
                }
            }
        }
    }
}
