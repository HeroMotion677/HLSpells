package com.divinity.hlspells.items;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellTypes;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spells.SpellActions;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;

public class SpellHoldingItem extends ProjectileWeaponItem {
    private final boolean isSpellBook;
    private Spell currentStoredSpell = null;

    public SpellHoldingItem(Properties properties, boolean isSpellBook) {
        super(properties);
        this.isSpellBook = isSpellBook;
    }

    public boolean isSpellBook() {
        return isSpellBook;
    }

    /**
     * Returns true if wand or staff
     */
    public boolean isWand() {
        return !isSpellBook;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> text, TooltipFlag pFlag) {
        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap -> {
            List<String> spells = cap.getSpells();
            if (isSpellBook) {
                Spell spell = SpellUtils.getSpell(stack);
                text.add(spell.getDisplayName().withStyle(spell.getType().getTooltipFormatting()));
            } else {
                text.add(1, new TextComponent(ChatFormatting.GOLD + "Spells: "));
                if (spells.isEmpty()) {
                    text.add(new TextComponent(ChatFormatting.GRAY + "   Empty"));
                } else {
                    spells.forEach(c -> {
                        Spell spell = SpellUtils.getSpellByID(c);
                        if (cap.getCurrentSpell().equals(c)) {
                            text.add(new TextComponent(ChatFormatting.BLUE + "   " + spell.getTrueDisplayName()));
                        } else {
                            text.add(new TextComponent(ChatFormatting.GRAY + "   " + spell.getTrueDisplayName()));
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        List<String> spells = pStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getSpells).orElse(null);
        return isSpellBook && SpellUtils.getSpell(pStack) != SpellInit.EMPTY.get() || !isSpellBook && spells != null && !spells.isEmpty() || super.isFoil(pStack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new SpellHolderProvider();
    }

    @Override
    public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
        if (isSpellBook) {
            if (this.allowdedIn(pGroup)) {
                for (Spell spell : SpellInit.SPELLS_REGISTRY.get()) {
                    ItemStack stack = new ItemStack(this);
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                        if (spell != SpellInit.EMPTY.get())
                            cap.addSpell(spell.getRegistryName().toString());
                    });
                    pItems.add(stack);
                }
            }
        } else {
            super.fillItemCategory(pGroup, pItems);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        currentStoredSpell = SpellUtils.getSpell(itemstack);
        LazyOptional<ISpellHolder> capability = itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (capability.isPresent()) {
            List<String> spells = capability.map(ISpellHolder::getSpells).orElse(null);
            if (spells != null && !(spells.isEmpty())) {
                player.startUsingItem(hand);
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
        capability.ifPresent(cap -> cap.setHeldActive(true));
        if (!world.isClientSide()) {
            if (isSpellBook)
                world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 0.6F, 1.0F);

            Spell spell = SpellUtils.getSpell(itemstack);
            if (spell != SpellInit.EMPTY.get() && spell.getType() == SpellTypes.HELD) {
                world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.NEUTRAL, 0.6F, 1.0F);
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pItemSlot, boolean pIsSelected) {
        if (entity instanceof Player player && stack.getItem() instanceof SpellHoldingItem) {
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                if (cap.isHeldActive()) {
                    for (InteractionHand hand : InteractionHand.values()) {
                        ItemStack heldStack = player.getItemInHand(hand);
                        if (SpellUtils.getSpell(heldStack) == currentStoredSpell)
                            return;
                    }
                    cap.setHeldActive(false);
                }
            });
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int power) {
        if (entity instanceof Player player) {
            LazyOptional<ISpellHolder> capability = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
            capability.ifPresent(cap -> cap.setHeldActive(false));
            if (!world.isClientSide() && castTimeCondition(player, stack)) {
                RunSpells.doCastSpell(player, world, stack);
                capability.filter(p -> !(p.getSpells().isEmpty())).ifPresent(cap -> {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL, 0.6F, 1.0F);
                    SpellActions.doParticles(player);
                    if (stack.getItem() instanceof StaffItem item) {
                        if (item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellTypes.MarkerTypes.COMBAT) {
                            player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 20));
                        } else if (!item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellTypes.MarkerTypes.UTILITY) {
                            player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 20));
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return isSpellBook ? UseAnim.CROSSBOW : UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return isSpellBook;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (EnchantedBookItem.getEnchantments(book).size() > 0) {
            for (int i = 0; i < EnchantedBookItem.getEnchantments(book).size(); i++) {
                CompoundTag tag = EnchantedBookItem.getEnchantments(book).getCompound(i);
                ResourceLocation enchantment = EnchantmentHelper.getEnchantmentId(tag);
                if (enchantment != null) {
                    if (enchantment.toString().contains("minecraft:mending") && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) == 0) {
                        return !isSpellBook || stack.getItem() instanceof StaffItem;
                    }
                    else if (enchantment.toString().contains("minecraft:unbreaking")) {
                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) <= EnchantmentHelper.getEnchantmentLevel(tag)) {
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) != 3) {
                                return !isSpellBook || stack.getItem() instanceof StaffItem;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return isSpellBook && SpellUtils.getSpell(stack) == SpellInit.EMPTY.get() && enchantment instanceof ISpell;
    }

    // Responsible for syncing capability to client side
    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag capTag = new CompoundTag();
        LazyOptional<ISpellHolder> spellHolder = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (spellHolder.isPresent()) {
            spellHolder.ifPresent(cap -> capTag.put("spellHolder", stack.serializeNBT()));
        }
        CompoundTag stackTag = stack.getTag();
        if (capTag.isEmpty()) {
            return stackTag;
        }
        if (stackTag == null) {
            stackTag = new CompoundTag();
        } else {
            stackTag = stackTag.copy(); //Because we don't actually want to add this data to the server side ItemStack
        }
        stackTag.put("spellCap", capTag);
        return stackTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        stack.setTag(nbt);
        if (nbt != null && nbt.contains("spellCap")) {
            CompoundTag capTags = nbt.getCompound("spellCap");
            if (capTags.contains("spellHolder")) {
                stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> stack.deserializeNBT(nbt));
            }
        }
    }

    private boolean castTimeCondition(Player player, ItemStack stack) {
        if (stack.getItem() instanceof StaffItem item) {
            return player.getUseItemRemainingTicks() < (72000 - (item.getCastDelay()));
        } else if (!isSpellBook) {
            return player.getUseItemRemainingTicks() < (72000 - (HLSpells.CONFIG.spellCastTime.get() * 20));
        }
        return player.getUseItemRemainingTicks() < 71988;
    }
}
