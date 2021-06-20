package com.heromotion.hlspells.items;

import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.misc.Spells;
import com.heromotion.hlspells.util.SpellUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class SpellBook extends ShootableItem implements IVanishable {

    public SpellBook() {
        super(new Properties()
                .stacksTo(1)
                .tab(ItemGroup.TAB_TOOLS));
    }

    public ItemStack getDefaultInstance() {
        return SpellUtils.setSpellBook(super.getDefaultInstance(), SpellBookInit.EMPTY.get());
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag) {
        SpellUtils.addSpellBookTooltip(stack, text, 1.0F);
    }

    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !SpellUtils.getSpell(stack).isEmpty();
    }

    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (com.heromotion.hlspells.spell.SpellBook spellBook : SpellBookInit.SPELL_BOOK_REGISTRY.get()) {
                stacks.add(SpellUtils.setSpellBook(new ItemStack(this), spellBook));
            }
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int power) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;

            int i = this.getUseDuration(stack) - power;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, world, playerEntity, i, true);
            float f = getPowerForTime(i);

            if (!((double) f < 0.1D)) {
                if (!world.isClientSide) {
                    Spells.doSpell(world, playerEntity, stack);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        // Spells.doSpell(worldIn, playerIn, itemstack);
        // return ActionResult.success(itemstack);
        playerIn.startUsingItem(handIn);
        return ActionResult.success(itemstack);
    }

    public static float getPowerForTime(int time) {
        float f = (float) time / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.CROSSBOW;
    }
}
