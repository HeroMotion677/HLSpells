package com.divinity.hlspells.items;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spells.SpellActions;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SpellHoldingItem extends ShootableItem {
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
    public void appendHoverText(ItemStack stack, @Nullable World pLevel, List<ITextComponent> text, ITooltipFlag pFlag) {
        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap -> {
            List<String> spells = cap.getSpells();
            if (isSpellBook) {
                Spell spell = SpellUtils.getSpell(stack);
                text.add(spell.getDisplayName().withStyle(spell.getType().getTooltipFormatting()));
            } else {
                text.add(1, new StringTextComponent(TextFormatting.GOLD + "Spells: "));
                if (spells.isEmpty()) {
                    text.add(new StringTextComponent(TextFormatting.GRAY + "   Empty"));
                } else {
                    spells.forEach(c -> {
                        Spell spell = SpellUtils.getSpellByID(c);
                        if (cap.getCurrentSpell().equals(c)) {
                            text.add(new StringTextComponent(TextFormatting.BLUE + "   " + spell.getTrueDisplayName()));
                        } else {
                            text.add(new StringTextComponent(TextFormatting.GRAY + "   " + spell.getTrueDisplayName()));
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
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new SpellHolderProvider();
    }

    @Override
    public void fillItemCategory(ItemGroup pGroup, NonNullList<ItemStack> pItems) {
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        currentStoredSpell = SpellUtils.getSpell(itemstack);
        LazyOptional<ISpellHolder> capability = itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (capability.isPresent()) {
            List<String> spells = capability.map(ISpellHolder::getSpells).orElse(null);
            if (spells != null && !(spells.isEmpty())) {
                player.startUsingItem(hand);
            } else {
                return ActionResult.pass(itemstack);
            }
        }
        capability.ifPresent(cap -> cap.setHeldActive(true));
        if (!world.isClientSide()) {
            if (isSpellBook)
                world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 0.6F, 1.0F);

            Spell spell = SpellUtils.getSpell(itemstack);
            if (spell != SpellInit.EMPTY.get() && spell.getType() == SpellType.HELD) {
                world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            }
        }
        return ActionResult.success(itemstack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int pItemSlot, boolean pIsSelected) {
        if (entity instanceof PlayerEntity && stack.getItem() instanceof SpellHoldingItem) {
            PlayerEntity player = (PlayerEntity) entity;
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                if (cap.isHeldActive()) {
                    for (Hand hand : Hand.values()) {
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
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int power) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            LazyOptional<ISpellHolder> capability = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
            capability.ifPresent(cap -> cap.setHeldActive(false));
            if (!world.isClientSide() && !isSpellBook ? (player.getUseItemRemainingTicks() < (72000 - (HLSpells.CONFIG.spellCastTime.get() * 20))) : player.getUseItemRemainingTicks() < 71988) {
                RunSpells.doCastSpell(player, world, stack);
                capability.filter(p -> !(p.getSpells().isEmpty()))
                        .ifPresent(cap -> {
                            world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                            SpellActions.doParticles(player);
                        });
            }
        }
    }


    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return isSpellBook ? UseAction.CROSSBOW : UseAction.BOW;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return isSpellBook;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return (!isSpellBook && EnchantedBookItem.getEnchantments(book).getString(0).contains("minecraft:mending")
                || !isSpellBook && EnchantedBookItem.getEnchantments(book).getString(0).contains("minecraft:unbreaking"));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return isSpellBook && SpellUtils.getSpell(stack) == SpellInit.EMPTY.get() && enchantment instanceof ISpell;
    }

    // Responsible for syncing capability to client side
    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT capTag = new CompoundNBT();
        LazyOptional<ISpellHolder> spellHolder = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (spellHolder.isPresent()) {
            spellHolder.ifPresent(cap -> capTag.put("spellHolder", Objects.requireNonNull(SpellHolderProvider.SPELL_HOLDER_CAP.writeNBT(spellHolder.orElseThrow(RuntimeException::new), null))));
        }
        CompoundNBT stackTag = stack.getTag();
        if (capTag.isEmpty()) {
            return stackTag;
        }
        if (stackTag == null) {
            stackTag = new CompoundNBT();
        } else {
            stackTag = stackTag.copy(); //Because we don't actually want to add this data to the server side ItemStack
        }
        stackTag.put("spellCap", capTag);
        return stackTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        stack.setTag(nbt);
        if (nbt != null && nbt.contains("spellCap")) {
            CompoundNBT capTags = nbt.getCompound("spellCap");
            if (capTags.contains("spellHolder")) {
                stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> SpellHolderProvider.SPELL_HOLDER_CAP.readNBT(spellHolder, null, capTags.get("spellHolder")));
            }
        }
    }
}
