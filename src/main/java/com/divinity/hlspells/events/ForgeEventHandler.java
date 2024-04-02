package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.items.totems.ITotem;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.serverbound.WandInputPacket;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.spells.Phasing;
import com.divinity.hlspells.spell.spells.PhasingII;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.util.Util;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Collection;
import java.util.Iterator;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.FORGE)
public class ForgeEventHandler {

    public static final KeyMapping WAND_BINDING = new KeyMapping("Next Spell", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");
    public static boolean buttonPressedFlag;
    public static boolean soulBond = false;
    public static boolean displayActivationOnDeath = false;

    @SubscribeEvent
    public static void onAttachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !event.getObject().getCapability(PlayerCapProvider.PLAYER_CAP).isPresent()) {
            event.addCapability(new ResourceLocation(MODID, "playereffectcap"), new PlayerCapProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachItemStackCaps(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack item = event.getObject();
        if (item.getItem() == ItemInit.TOTEM_OF_RETURNING.get() || item.getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
            event.addCapability(new ResourceLocation(MODID, "totemcap"), new TotemItemProvider());
        }
        if (HLSpells.isCurioLoaded) {
            CuriosCompat.attachCapabilities(event);
        }
    }

    @SubscribeEvent
    public static void registerLoot(LootTableLoadEvent evt) {
        String prefix = "minecraft:chests/";
        String name = evt.getName().toString();
        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch (file) {
                case "woodland_mansion", "end_city_treasure", "stronghold_library", "jungle_temple", "simple_dungeon", "desert_pyramid", "nether_bridge", "bastion_treasure", "igloo_chest", "ancient_city" -> evt.getTable().addPool(getInjectPool(file));
                default -> {}
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
                    .anyMatch(p -> EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), p) > 0);

            ItemInit.TOTEMS.forEach(totem -> checkAllTotemSlots(player, event, totem.get(), HLSpells.isCurioLoaded));

            if (soulBond) {
                player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    int size = player.inventory.compartments.stream().mapToInt(NonNullList::size).sum();
                    for (int i = 0; i < size; i++) {
                        ItemStack stack = player.inventory.getItem(i);
                        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) > 0)
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
            for (Iterator<ItemEntity> itemEntityIterator = event.getDrops().iterator(); itemEntityIterator.hasNext();) {
                ItemStack stack = itemEntityIterator.next().getItem();
                // TOTEM OF KEEPING (Reloads player inventory even after dying and disables inventory from spilling)
                if (stack.getItem() == ItemInit.TOTEM_OF_KEEPING.get() && !keepingTotem[0]) {
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap -> {
                        InteractionHand hand = cap.getTotemInHand();
                        if (hand == InteractionHand.MAIN_HAND || hand == InteractionHand.OFF_HAND) {
                            player.inventory.load(cap.getInventoryNBT());
                            if (HLSpells.isCurioLoaded) {
                                CuriosCompat.restoreCuriosInv(player, cap.getCuriosNBT());
                                CuriosCompat.getItemInCuriosSlot(player, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(slotContext -> slotContext.stack().getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(totemCap -> {
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
                    stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap -> {
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
                            }
                            else if (hand == InteractionHand.OFF_HAND) {
                                player.inventory.offhand.set(0, stack);
                                itemEntityIterator.remove();
                                cap.setTotemInHand(null);
                            }
                        }
                    });
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) > 0 && !keepingTotem[0]) {
                    itemEntityIterator.remove();
                }
            }
            if (!keepingTotem[0]) {
                // Present here to show the soul bond items on respawn screen.
                player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> cap.getSoulBondItems().forEach((pIndex, pStack) -> {
                    if (player.inventory.getItem(pIndex).isEmpty()) {
                        player.inventory.setItem(pIndex, pStack);
                    }
                    else player.inventory.add(pStack);
                }));
            }
            else {
                event.getDrops().removeIf(itemEntity -> {
                    if (player.inventory.contains(itemEntity.getItem())) return true;
                    return HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(player, itemEntity.getItem().getItem()).isPresent();
                });
            }
        }
    }

    @SubscribeEvent
    public static void onEntityCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath() && !event.getEntity().level.isClientSide()) {
            Player original = event.getOriginal();
            Player current = event.getEntity();
            boolean keepingActivated = false;
            // TOTEM OF KEEPING (Restores the inventory)
            original.reviveCaps(); // This is needed to re-validate original player's capabilities (only affects newer versions apparently)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                int mainSlot = -1;
                Inventory inv = original.inventory;
                for (int i = 0; i < inv.items.size(); ++i) {
                    ItemStack stackInSlot = inv.items.get(i);
                    if (!stackInSlot.isEmpty() && original.getMainHandItem().getItem() == stackInSlot.getItem() &&
                            ItemStack.tagMatches(original.getMainHandItem(), stackInSlot)) {
                        mainSlot = i;
                    }
                }
                original.inventory.getItem(mainSlot != -1 ? mainSlot : 0).shrink(mainSlot != -1 ? 1 : 0); // Wtf?
                keepingActivated = true;
            }
            else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_KEEPING.get()) {
                original.inventory.offhand.get(0).shrink(1);
                keepingActivated = true;
            }
            else if (HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_KEEPING.get()).isPresent()) {
                CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_KEEPING.get()).ifPresent(slotResult -> slotResult.stack().getCapability(TotemItemProvider.TOTEM_CAP).ifPresent(cap -> {
                    if (cap.diedTotemInCurios())
                        slotResult.stack().shrink(1);
                }));
                CuriosCompat.restoreCuriosInv(current, CuriosCompat.getCuriosInv(original));
                keepingActivated = true;
            }
            if (keepingActivated) {
                current.inventory.replaceWith(original.inventory);
                current.level.broadcastEntityEvent(current, (byte) 35);
                displayActivationOnDeath = true;
            }
            // TOTEM OF RETURNING (Adds totem to the inventory)
            if (original.getMainHandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.setItem(original.inventory.selected, original.inventory.getSelected());
            }
            else if (original.getOffhandItem().getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                current.inventory.offhand.set(0, original.inventory.offhand.get(0));
            }
            else if (HLSpells.isCurioLoaded && CuriosCompat.getItemInCuriosSlot(original, ItemInit.TOTEM_OF_RETURNING.get()).isPresent()) {
                CuriosCompat.restoreCuriosInv(current, CuriosCompat.getCuriosInv(original));
            }
            // SOUL BOND
            if (!keepingActivated) {
                original.getCapability(PlayerCapProvider.PLAYER_CAP).filter(p -> !p.getSoulBondItems().isEmpty()).ifPresent(cap -> {
                    cap.getSoulBondItems().forEach((pIndex, pStack) -> {
                        if (current.inventory.getItem(pIndex).isEmpty()) {
                            current.inventory.setItem(pIndex, pStack);
                        }
                        else current.inventory.add(pStack);
                    });
                    cap.getSoulBondItems().clear();
                });
            }
            original.invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide()) {
            if (event.phase == TickEvent.Phase.END && displayActivationOnDeath) {
                displayActivationOnDeath = false;
                Util.displayActivation(event.player, ItemInit.TOTEM_OF_KEEPING.get());
            }
        }
    }

    // TOTEM OF RETURNING (Teleports the player to last died pos when right-clicked)
    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() != null) {
            Player player = event.getEntity();
            Level world = player.level;
            if (!world.isClientSide()) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (stack.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                        stack.getCapability(TotemItemProvider.TOTEM_CAP).filter(ITotemCap::getHasDied).ifPresent(cap -> {
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
                    next.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                        player.displayClientMessage(Component.literal(spell.getTrueDisplayName()).withStyle(ChatFormatting.AQUA), true);
                    });
                }
                if (previous.getItem() instanceof SpellHoldingItem item) {
                    previous.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).filter(cap -> !cap.getSpells().isEmpty()).ifPresent(cap -> {
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

    public static final EntityDimensions OLD_PLAYER_DIMENSIONS = new EntityDimensions(0.6f, 1.8f, false);
    public static final EntityDimensions SHRINK_DIMENSIONS = new EntityDimensions(0.6f, 0.8f, true);

    @SubscribeEvent
    public static void onTinyPlayer(EntityEvent.Size event) {
/*        if (event.getEntity().isAddedToWorld()) {
            if (event.getEntity() instanceof Player player) {
                Spell spell = SpellUtils.getSpell(player.getUseItem());
                if (spell == SpellInit.SHRINK.get() && spell.canUseSpell()) {
                    event.setNewSize(SHRINK_DIMENSIONS, false);
                    event.setNewEyeHeight(player.isCrouching() ? 0.55F : 0.7F);
                }
                else if (SpellUtils.getSpell(player.getUseItem()) != SpellInit.SHRINK.get()) {
                    event.setNewSize(OLD_PLAYER_DIMENSIONS, true);
                }
            }
        }*/
    }

    @SubscribeEvent
    public static void clearEffectsAfterUse(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player != null && !player.level.isClientSide()) {
            if (event.phase == TickEvent.Phase.END) {
                if (!(player.getUseItem().getItem() instanceof SpellHoldingItem)) {
                    Util.clearEffects(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void preventPhasingSuffocation(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell() || SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell2 && spell2.canUseSpell()) {
                    if (event.getSource() == DamageSource.IN_WALL) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool().add(getInjectEntry(entryName)).setBonusRolls(BinomialDistributionGenerator.binomial(0, 1)).name("inject").build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(HLSpells.MODID, "inject/" + name);
        return LootTableReference.lootTableReference(table).setWeight(1);
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
                    targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, false);
                }
                if (off.getItem() == target && compatibleTotems) {
                    compatibleTotems = !(isSameTotem(off, main) || curiosStack != null && isSameTotem(off, curiosStack));
                    targetTotem.performAction(event, player, player.level, off, InteractionHand.OFF_HAND, false);
                }
                if (curiosStack != null) {
                    if (curiosStack.getItem() == target && compatibleTotems) {
                        targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, true);
                    }
                }
            }
            else if (targetTotem.doesCancelDeath()) {
                if (main.getItem() == target) {
                    targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, false);
                }
                else if (off.getItem() == target) {
                    targetTotem.performAction(event, player, player.level, off, InteractionHand.OFF_HAND, false);
                }
                else if (curiosStack != null) {
                    if (curiosStack.getItem() == target) {
                        targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, true);
                    }
                }
            }
            else if (!targetTotem.isDisposableOnDeath() && !targetTotem.doesCancelDeath() && !event.isCanceled()) {
                if (main.getItem() == target) {
                    otherTotems = !(isSameTotem(main, off) || curiosStack != null && isSameTotem(main, curiosStack));
                    targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, false);
                }
                if (off.getItem() == target && otherTotems) {
                    otherTotems = !(isSameTotem(off, main) || curiosStack != null && isSameTotem(off, curiosStack));
                    targetTotem.performAction(event, player, player.level, off, InteractionHand.OFF_HAND, false);
                }
                if (curiosStack != null) {
                    if (curiosStack.getItem() == target && otherTotems) {
                        targetTotem.performAction(event, player, player.level, main, InteractionHand.MAIN_HAND, true);
                    }
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
    public static class ClientEventHandler {

        @SubscribeEvent
        public static void onRenderBlockOnHUD(RenderBlockScreenEffectEvent event) {
            Player player = event.getPlayer();
            if (player != null && player.isUsingItem()) {
                if (SpellUtils.getSpell(player.getUseItem()) instanceof Phasing spell && spell.canUseSpell() || SpellUtils.getSpell(player.getUseItem()) instanceof PhasingII spell2 && spell2.canUseSpell()) {
                    event.setCanceled(true);
                }
            }
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (WAND_BINDING.consumeClick()) {
                    if (player != null && !player.isUsingItem()) {
                        NetworkManager.INSTANCE.sendToServer(new WandInputPacket(WAND_BINDING.getKey().getValue()));
                        for (InteractionHand hand : InteractionHand.values()) {
                            ItemStack carriedItem = player.getItemInHand(hand);
                            if (carriedItem.getItem() instanceof SpellHoldingItem item && !item.isSpellBook()) {
                                carriedItem.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                                    if (!cap.getSpells().isEmpty()) {
                                        cap.incrementCurrentSpellCycle();
                                        Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
                                        player.displayClientMessage(Component.literal(spell.getTrueDisplayName()).withStyle(ChatFormatting.AQUA), true);
                                    }
                                });
                                break;
                            }
                        }
                    }
                    buttonPressedFlag = true;
                }
                buttonPressedFlag = WAND_BINDING.isDown() || !buttonPressedFlag;
            }
        }

        /**
         * When a spell holding item is used it stops the slowness effect
         */
        @SubscribeEvent
        @SuppressWarnings("ConstantConditions")
        public static void onInput(MovementInputUpdateEvent event) {
            if (event.getEntity() instanceof LocalPlayer player) {
                InteractionHand hand = player.getUsedItemHand();
                // Don't remove this even if it complains. If it can be null, it can be null
                if (hand != null) {
                    ItemStack stack = player.getItemInHand(hand);
                    if (player.isUsingItem() && !player.isPassenger() && stack.getItem() instanceof SpellHoldingItem) {
                        Spell spell = SpellUtils.getSpell(stack);
                        if (spell == SpellInit.SPEED.get() || spell == SpellInit.FROST_PATH_II.get() || spell == SpellInit.FROST_PATH.get() || spell == SpellInit.PHASING.get() || spell == SpellInit.PHASING_II.get()) {
                            player.input.leftImpulse /= 0.2F;
                            player.input.forwardImpulse /= 0.2F;
                        }
                    }
                }
            }
        }
    }
}
