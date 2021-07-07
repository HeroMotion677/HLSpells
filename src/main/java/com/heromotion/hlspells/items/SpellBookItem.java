package com.heromotion.hlspells.items;


import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.init.SpellInit;
import com.heromotion.hlspells.misc.CastSpells;
import com.heromotion.hlspells.spell.Spell;
import com.heromotion.hlspells.spell.SpellBookObject;
import com.heromotion.hlspells.spell.SpellType;
import com.heromotion.hlspells.util.SpellUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;



public class SpellBookItem extends ShootableItem
{
    public static boolean isHeldActive = false;

    public SpellBookItem()
    {
        super(new Properties()
                .stacksTo(1)
                .tab(ItemGroup.TAB_TOOLS));
    }

    public ItemStack getDefaultInstance()
    {
        return SpellUtils.setSpellBook(super.getDefaultInstance(), SpellBookInit.EMPTY.get());
    }

    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag) {
        SpellUtils.addSpellBookTooltip(stack, text, 1.0F);
    }

    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !SpellUtils.getSpell(stack).isEmpty();
    }

    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (SpellBookObject spellBookObject : SpellBookInit.SPELL_BOOK_REGISTRY.get()) {
                stacks.add(SpellUtils.setSpellBook(new ItemStack(this), spellBookObject));
            }
        }
    }

    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    public int getDefaultProjectileRange() {
        return 8;
    }

    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int power)
    {
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            isHeldActive = false;

            if (playerEntity.getUseItemRemainingTicks() < 71984)
            {
                if (!playerEntity.getCommandSenderWorld().isClientSide())
                {
                    CastSpells.doCastSpell(playerEntity, world, stack);
                }

                if (playerEntity.getCommandSenderWorld().isClientSide())
                {
                    CastSpells.doParticles(playerEntity);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> use (World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        isHeldActive = true;
        return ActionResult.success(itemstack);
    }

    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.CROSSBOW;
    }
}