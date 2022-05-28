package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.world.blocks.AltarOfAttunementBlock;
import com.divinity.hlspells.world.blocks.CustomFrostedIce;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HLSpells.MODID);

    public static final RegistryObject<Block> CUSTOM_FROSTED_ICE = BLOCKS.register("frosted_ice", () -> new CustomFrostedIce(BlockBehaviour.Properties.of(Material.ICE).friction(0.98F).randomTicks().strength(0.5F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> ALTAR_OF_ATTUNEMENT_BLOCK = BLOCKS.register("altar_of_attunement", () ->
            new AltarOfAttunementBlock((BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_RED)
                    .requiresCorrectToolForDrops()
                    .lightLevel(level -> 10).strength(5.0F, 1200.0F))));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, HLSpells.MODID);

    @SuppressWarnings("all")
    public static final RegistryObject<BlockEntityType<AltarOfAttunementBE>> ALTAR_BE = BLOCK_ENTITIES.register("altar_of_attunement", () -> BlockEntityType.Builder.of(AltarOfAttunementBE::new, ALTAR_OF_ATTUNEMENT_BLOCK.get()).build(null));
}
