package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.blocks.CustomFrostedIce;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HLSpells.MODID);
    public static final RegistryObject<Block> CUSTOM_FROSTED_ICE = BLOCKS.register("frosted_ice", () -> new CustomFrostedIce(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(0.5F).sound(SoundType.GLASS).noOcclusion()));
}
