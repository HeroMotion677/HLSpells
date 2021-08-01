package com.divinity.hlspells.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class WandItem extends ShootableItem
{
    public WandItem (Properties properties)
    {
        super(properties);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles()
    {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange()
    {
        return 8;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
    {
        return super.use(world, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {

        super.inventoryTick(stack, world, entity, p_77663_4_, p_77663_5_);
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_)
    {
        return UseAction.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_)
    {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack p_77615_1_, World p_77615_2_, LivingEntity p_77615_3_, int p_77615_4_)
    {
        super.releaseUsing(p_77615_1_, p_77615_2_, p_77615_3_, p_77615_4_);
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_)
    {
        return super.isFoil(p_77636_1_);
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_)
    {
        return super.getRarity(p_77613_1_);
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_)
    {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack)
    {
        return super.isRepairable(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }
}
