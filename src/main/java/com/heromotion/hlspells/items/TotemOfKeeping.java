package com.heromotion.hlspells.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.*;

import net.minecraftforge.api.distmarker.*;

public class TotemOfKeeping extends Item {

    public TotemOfKeeping() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .tab(ItemGroup.TAB_MISC));
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 0;
    }

    @Override
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1F;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }
}
