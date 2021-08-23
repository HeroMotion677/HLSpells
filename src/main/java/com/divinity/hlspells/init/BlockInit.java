package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.blocks.CustomFrostedIce;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HLSpells.MODID);
    public static final RegistryObject<Block> CUSTOM_FROSTED_ICE = BLOCKS.register("frosted_ice", () -> new CustomFrostedIce(AbstractBlock.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(0.5F).sound(SoundType.GLASS).noOcclusion()));
}
