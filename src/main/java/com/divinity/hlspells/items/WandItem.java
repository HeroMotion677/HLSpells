package com.divinity.hlspells.items;

import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.WandItemProvider;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spells.SpellActions;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.block.GrindstoneBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.GrindstoneContainer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Predicate;


public class WandItem extends ShootableItem
{
    public static boolean isWandHeldActive = false;

    public WandItem (Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        return new ItemStack(this);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks)
    {
        if (this.allowdedIn(group))
        {
            stacks.add(new ItemStack(this));
        }
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
    public ActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);;
        isWandHeldActive = true;
        return ActionResult.success(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity =  (PlayerEntity) entity;
            if (isWandHeldActive)
            {
                if (playerEntity.getMainHandItem().getItem() instanceof WandItem || playerEntity.getOffhandItem().getItem() instanceof WandItem)
                {
                    return;
                }
                isWandHeldActive = false;
            }
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_)
    {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int p_77615_4_)
    {
        stack.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(p -> {
            System.out.println(p.getSpells());
        });

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            isWandHeldActive = false;

            if (playerEntity.getUseItemRemainingTicks() < 71988)
            {
                if (!playerEntity.getCommandSenderWorld().isClientSide())
                {
                    RunSpells.doCastSpell(playerEntity, world, stack);
                }

                if (playerEntity.getCommandSenderWorld().isClientSide())
                {
                    SpellActions.doParticles(playerEntity);
                }
            }
        }
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
    public boolean isRepairable(ItemStack stack)
    {
        return super.isRepairable(stack);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }

}
