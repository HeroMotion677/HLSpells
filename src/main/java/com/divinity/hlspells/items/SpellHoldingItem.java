package com.divinity.hlspells.items;

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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
        return isSpellBook && SpellUtils.getSpell(pStack) != SpellInit.EMPTY.get();
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
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(spell.getRegistryName().toString()));
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
        player.startUsingItem(hand);
        currentStoredSpell = SpellUtils.getSpell(itemstack);
        itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(true));
        if (!world.isClientSide()) {
            if (player.getUseItemRemainingTicks() < 71997 && player.getUseItemRemainingTicks() >= 71994 && isSpellBook) {
                world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            }

            Spell spell = SpellUtils.getSpell(itemstack);
            if (spell != SpellInit.EMPTY.get() && player.getUseItemRemainingTicks() < 71994 && player.getUseItemRemainingTicks() >= 71991) {
                if (spell.getType() == SpellType.CAST) {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                } else if (spell.getType() == SpellType.HELD) {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
            }
        }
        return ActionResult.success(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int pItemSlot, boolean pIsSelected) {
        if (isSpellBook && stack.getEnchantmentTags().size() > 1) {
            for (int i = 0; i < stack.getEnchantmentTags().size() && stack.getEnchantmentTags().size() > 1; i++) {
                stack.getEnchantmentTags().remove(i);
            }
        }
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
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(false));
            if (player.getUseItemRemainingTicks() < 71988) {
                if (!world.isClientSide()) {
                    RunSpells.doCastSpell(player, world, stack);
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
                SpellActions.doParticles(player);
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
        return isSpellBook || super.isEnchantable(pStack);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return isSpellBook && super.isBookEnchantable(stack, book);
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
            stackTag = stackTag.copy(); //Because we dont actually want to add this data to the server side ItemStack
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
