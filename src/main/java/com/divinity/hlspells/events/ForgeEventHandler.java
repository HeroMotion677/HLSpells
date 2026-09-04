package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.items.totems.ITotem;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.spells.Phasing;
import com.divinity.hlspells.spell.spells.PhasingII;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Collection;
import java.util.Iterator;

import static com.divinity.hlspells.HLSpells.MODID;

@EventBusSubscriber(modid = HLSpells.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ForgeEventHandler {

    public static boolean soulBond = false;
    public static boolean displayActivationOnDeath = false;

    @SubscribeEvent
    public static void registerLoot(LootTableLoadEvent evt) {
        String prefix = "minecraft:chests/";
        String name = evt.getName().toString();
        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch (file) {
                case "woodland_mansion", "end_city_treasure", "stronghold_library", "jungle_temple", "simple_dungeon", "desert_pyramid", "nether_bridge", "bastion_treasure", "igloo_chest", "ancient_city", "ancient_city_ice_box", "bastion_other", "bastion_bridge" ->
                        evt.getTable().addPool(getInjectPool(file));
                default -> {
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            // Check if any item in the player's inventory contains soul bond enchant
            soulBond = player.inventory.compartments
                    .stream()
                    .flatMap(Collection::stream) // Flatmap to reduce overhead
                    .anyMatch(p -> EnchantmentInit.getLevel(EnchantmentInit.SOUL_BOND, p) > 0);

            ItemInit.TOTEMS.forEach(totem -> checkAllTotemSlots(player, event, totem.get(), HLSpells.isCurioLoaded));

            if (soulBond) {
                PlayerCapProvider.get(player).ifPresent(cap -> {
                    int size = player.inventory.compartments.stream().mapToInt(NonNullList::size).sum();
                    for (int i = 0; i < size; i++) {
                        ItemStack stack = player.inventory.getItem(i);
                        if (EnchantmentInit.getLevel(EnchantmentInit.SOUL_BOND, stack) > 0)
                            cap.addSoulBondItem(i, stack);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player) {
            // If true the inventory is loaded back and drops are removed
            boolean[] keepingTotem = new boolean[1];
            for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext(); ) {
                ItemStack stack = itemEntityIterator.next().getItem();
                // TOTEM OF KEEPING (Reloads player inventory even after dying and disables inventory from spilling)
                if (stack.getItem() == ItemInit.TOTEM_OF_KEEPING.get() && !keepingTotem[0]) {
                    TotemItemProvider.get(stack).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                        InteractionHand hand = cap.getTotemInHand();
                        if (hand == InteractionHand.MAIN_HAND || hand == InteractionHand.OFF_HAND) {
                            player.inventory.load(cap.getInventoryNBT());
                            if (HLSpells.isCurioLoaded) {
                                CuriosCompat.restoreCuriosInv(player, cap.getCuriosNBT());
                                CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(slotContext -> TotemItemProvider.get(slotContext.stack()).ifPresent(totemCap -> {
                                    totemCap.setDiedTotemInCurios(true);
                                    totemCap.setCuriosSlot(cap.getCuriosSlot());
                                }));
                            }
                            cap.setTotemInHand(null);
                            keepingTotem[0] = true;
                            itemEntityIterator.remove();
                        }
                    });
                }
                // TOTEM OF RETURNING (Sets BlockPos to teleportToLocation to and sets the slot the totem should be in)
                if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                    TotemItemProvider.get(stack).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                        InteractionHand hand = cap.getTotemInHand();
                        boolean returnInCurio = false;
                        if (HLSpells.isCurioLoaded && cap.diedTotemInCurios()) {
                            CuriosCompat.getStackHandler(player).ifPresent(stackHandler -> stackHandler.getStacks().setStackInSlot(cap.getCuriosSlot(), stack));
                            itemEntityIterator.remove();
                            cap.setTotemInHand(null);
                            returnInCurio = true;
                        }
                        // The drop is removed here to avoid deleting both totems held in both hands
                        if (!returnInCurio) {
                            if (hand == InteractionHand.MAIN_HAND) {
                                player.inventory.add(player.inventory.selected, stack);
                                itemEntityIterator.remove();
                                cap.setTotemInHand(null);
                            } else if (hand == InteractionHand.OFF_HAND) {
                                player.inventory.offhand.set(0, stack);
                                itemEntityIterator.remove();
                                cap.setTotemInHand(null);
                            }
                        }
                    });
                }
                if (EnchantmentInit.getLevel(EnchantmentInit.SOUL_BOND, stack) > 0 && !keepingTotem[0]) {
                    itemEntityIterator.remove();
                }
            }
            if (!keepingTotem[0]) {
                // Present here to show the soul bond items on respawn screen.
                PlayerCapProvider.get(player).ifPresent(cap -> cap.getSoulBondItems().forEach((pIndex, pStack) -> {
                    if (player.inventory.getItem(pIndex).isEmpty()) {
                        player.inventory.setItem(pIndex, pStack);
                    } else player.inventory.add(pStack);
                }));
            } else {
                event.getDrops().removeIf(itemEntity -> {
                    if (player.inventory.contains(itemEntity.getItem())) return true;
                    return HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(player, itemEntity.getItem().getItem()).isPresent();
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath() && !event.getEntity().level().isClientSide()) {
            Player original = event.getOriginal();
            Player current = event.getEntity();
            boolean keepingActivated = false;
            // TOTEM OF KEEPING (Restores the inventory)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                int mainSlot = -1;
                Inventory inv = original.inventory;
                for (int i = 0; i < inv.items.size(); ++i) {
                    ItemStack stackInSlot = inv.items.get(i);
                    if (!stackInSlot.isEmpty() && original.getMainHandItem().getItem() == stackInSlot.getItem() &&
                            ItemStack.isSameItemSameComponents(original.getMainHandItem(), stackInSlot)) {
                        mainSlot = i;
                    }
                }
                original.inventory.getItem(mainSlot != -1 ? mainSlot : 0).shrink(mainSlot != -1 ? 1 : 0); // Wtf?
                keepingActivated = true;
            } else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                original.inventory.offhand.get(0).shrink(1);
                keepingActivated = true;
            } else if (HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_KEEPING.get()).isPresent()) {
                CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(slotResult -> TotemItemProvider.get(slotResult.stack()).ifPresent(cap -> {
                    if (cap.diedTotemInCurios())
                        slotResult.stack().shrink(1);
                }));
                CuriosCompat.restoreCuriosInv(current, CuriosCompat.getCuriosInv(original));
                keepingActivated = true;
            }
            if (keepingActivated) {
                current.inventory.replaceWith(original.inventory);
                current.level().broadcastEntityEvent(current, (byte) 35);
                displayActivationOnDeath = true;
            }
            // TOTEM OF RETURNING (Adds totem to the inventory)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.setItem(original.inventory.selected, original.inventory.getSelected());
            } else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.offhand.set(0, original.inventory.offhand.get(0));
            } else if (HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_RETURNING.get()).isPresent()) {
                CuriosCompat.restoreCuriosInv(current, CuriosCompat.getCuriosInv(original));
            }
            // SOUL BOND
            if (!keepingActivated) {
                PlayerCapProvider.get(original).filter(p -> !p.getSoulBondItems().isEmpty()).ifPresent(cap -> {
                    cap.getSoulBondItems().forEach((pIndex, pStack) -> {
                        if (current.inventory.getItem(pIndex).isEmpty()) {
                            current.inventory.setItem(pIndex, pStack);
                        } else current.inventory.add(pStack);
                    });
                    cap.getSoulBondItems().clear();
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide()) {
            if (displayActivationOnDeath) {
                displayActivationOnDeath = false;
                Util.displayActivation(event.getEntity(), ItemInit.TOTEM_OF_KEEPING.get());
            }
        }
    }

    // TOTEM OF RETURNING (Teleports the player to last died pos when right-clicked)
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() != null) {
            Player player = event.getEntity();
            Level world = player.level();
            if (!world.isClientSide()) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                        TotemItemProvider.get(stack).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                            BlockPos pos = cap.getBlockPos();
                            Util.displayActivation(player, ItemInit.TOTEM_OF_RETURNING.get());
                            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.3F, 0.3F);
                            player.teleportTo(pos.getX(), pos.getY(), pos.getZ());
                            player.setItemInHand(hand, ItemStack.EMPTY);
                            Util.doTeleportParticles(world, pos, 200);
                            world.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5F, 0.7F);
                        });
                        return;
                    }
                }
            }
        }
    }

    // This is needed to prevent a loophole where using a held spell then switching to another slot doesn't proc the cooldown
    @SubscribeEvent
    public static void onLivingEquipChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSlot().getType() == EquipmentSlot.Type.HAND) {
                ItemStack next = event.getTo();
                ItemStack previous = event.getFrom();
                if (next.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                    SpellHolderProvider.get(next).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        player.displayClientMessage(Component.literal(spell.getTrueDisplayName()).withStyle(ChatFormatting.AQUA), true);
                    });
                }
                if (previous.getItem() instanceof SpellHoldingItem item) {
                    SpellHolderProvider.get(previous).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                        cap.setSpellSoundBuffer(0);
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        if (spell.getSpellType() == SpellAttributes.Type.HELD && item.isWasHolding() && !player.isUsingItem()) {
                            player.getCooldowns().addCooldown(previous.getItem(), 25);
                            item.setWasHolding(false);
                        }
                    });
                }
            }
        }
    }

    @SubscribeEvent
    public static void clearEffectsAfterUse(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player != null && !player.level().isClientSide()) {
            if (!(player.getUseItem().getItem() instanceof SpellHoldingItem)) {
                Util.clearEffects(player);
            }
        }
    }

    @SubscribeEvent
    public static void preventPhasingSuffocation(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell()) {
                    if (event.getSource() == player.damageSources().inWall()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void preventPhasingIISuffocation(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell && spell.canUseSpell()) {
                    if (event.getSource() == player.damageSources().inWall()) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).setBonusRolls(BinomialDistributionGenerator.binomial(0, 1)).build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        ResourceKey<LootTable> table = ResourceKey.create(Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "inject/" + name));
        return NestedLootTable.lootTableReference(table).setWeight(1);
    }

    private static boolean isSameTotem(ItemStack itemToCompare, ItemStack other) {
        return itemToCompare.getItem().getClass() == other.getItem().getClass();
    }

    public static void checkAllTotemSlots(Player player, LivingDeathEvent event, Item target, boolean isCurios) {
        if (target instanceof ITotem targetTotem) {
            // Responsible for stopping the same totem in offhand/curios being activated multiple times
            boolean compatibleTotems = true;
            boolean otherTotems = true;

            ItemStack main = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
            ItemStack curiosStack = isCurios ? CuriosCompat.getItemInCuriosSlot(player, p -> p.getItem() instanceof ITotem).map(SlotResult::stack).orElse(null) : null;

            if (targetTotem.isDisposableOnDeath()) {
                if (main.getItem() == target) {
                    compatibleTotems = !(isSameTotem(main, off) || curiosStack != null && isSameTotem(main, curiosStack));
                    targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, false);
                }
                if (off.getItem() == target && compatibleTotems) {
                    compatibleTotems = !(isSameTotem(off, main) || curiosStack != null && isSameTotem(off, curiosStack));
                    targetTotem.performAction(event, player, player.level(), off, InteractionHand.OFF_HAND, false);
                }
                if (curiosStack != null) {
                    if (curiosStack.getItem() == target && compatibleTotems) {
                        targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, true);
                    }
                }
            } else if (targetTotem.doesCancelDeath()) {
                if (main.getItem() == target) {
                    targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, false);
                } else if (off.getItem() == target) {
                    targetTotem.performAction(event, player, player.level(), off, InteractionHand.OFF_HAND, false);
                } else if (curiosStack != null) {
                    if (curiosStack.getItem() == target) {
                        targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, true);
                    }
                }
            } else if (!targetTotem.isDisposableOnDeath() && !targetTotem.doesCancelDeath() && !event.isCanceled()) {
                if (main.getItem() == target) {
                    otherTotems = !(isSameTotem(main, off) || curiosStack != null && isSameTotem(main, curiosStack));
                    targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, false);
                }
                if (off.getItem() == target && otherTotems) {
                    otherTotems = !(isSameTotem(off, main) || curiosStack != null && isSameTotem(off, curiosStack));
                    targetTotem.performAction(event, player, player.level(), off, InteractionHand.OFF_HAND, false);
                }
                if (curiosStack != null) {
                    if (curiosStack.getItem() == target && otherTotems) {
                        targetTotem.performAction(event, player, player.level(), main, InteractionHand.MAIN_HAND, true);
                    }
                }
            }
        }
    }
}
