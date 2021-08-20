package com.divinity.hlspells.items;

import com.divinity.hlspells.items.capabilities.wandcap.SpellHolderProvider;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;


public class WandItem extends ShootableItem {

    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
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
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag) {
        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).ifPresent(cap ->
        {
            text.add(1, new StringTextComponent(TextFormatting.GOLD + "Spells: "));
            if (cap.getSpells().isEmpty()) {
                text.add(new StringTextComponent(TextFormatting.GRAY + "   Empty"));
            } else {
                cap.getSpells().forEach(c ->
                {
                    Spell spell = SpellUtils.getSpellByID(c);
                    if (spell != null) {
                        if (cap.getCurrentSpell().equals(c)) {
                            text.add(new StringTextComponent(TextFormatting.BLUE + "   " + spell.getTrueDisplayName()));
                        } else {
                            text.add(new StringTextComponent(TextFormatting.GRAY + "   " + spell.getTrueDisplayName()));
                        }
                    }
                });
            }
        });
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(true));
        itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP, null).filter(p -> !p.getSpells().isEmpty()).ifPresent(cap -> {
            Spell spell = SpellUtils.getSpellByID(cap.getCurrentSpell());
            if (spell != null && !world.isClientSide()) {
                if (playerIn.getUseItemRemainingTicks() < 71994 && (double) playerIn.getUseItemRemainingTicks() >= 71991) {
                    if (spell.getType() == SpellType.CAST) {
                        world.playSound(null, playerIn.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                    } else if (spell.getType() == SpellType.HELD) {
                        world.playSound(null, playerIn.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                    }
                }
            }
        });
        return ActionResult.success(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int pItemSlot, boolean pIsSelected) {
        if (entity instanceof PlayerEntity && stack.getItem() instanceof WandItem) {
            PlayerEntity player = (PlayerEntity) entity;
            Predicate<ItemStack> isSpellBook = itemStack -> itemStack.getItem() instanceof WandItem;
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                if (cap.isHeldActive() && !isSpellBook.test(player.getMainHandItem()) && !isSpellBook.test(player.getOffhandItem())) {
                    cap.setHeldActive(false);
                }
            });
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int pTimeLeft) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(false));
            if (player.getUseItemRemainingTicks() < 71988) {
                if (!player.getCommandSenderWorld().isClientSide()) {
                    RunSpells.doCastSpell(player, world, stack);
                }
                SpellActions.doParticles(player);
            }
        }
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
