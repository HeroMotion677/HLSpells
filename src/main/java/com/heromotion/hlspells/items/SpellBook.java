package com.heromotion.hlspells.items;

import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.misc.Spells;
import com.heromotion.hlspells.util.SpellUtils;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import net.minecraftforge.api.distmarker.*;

import javax.annotation.Nullable;
import java.util.List;

public class SpellBook extends Item {

    public SpellBook() {
        super(new Properties()
                .stacksTo(1)
                .tab(ItemGroup.TAB_TOOLS));
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
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

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        Spells.doSpell(worldIn, playerIn, itemstack);
        return ActionResult.success(itemstack);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        ActionResultType action = super.onItemUseFirst(stack, context);
        Spells.doSpell(context.getLevel(), context.getPlayer(), stack);
        return action;
    }
}
