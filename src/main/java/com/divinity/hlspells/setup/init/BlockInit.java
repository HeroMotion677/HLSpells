package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.world.blocks.AltarOfAttunementBlock;
import com.divinity.hlspells.world.blocks.CustomFrostedIce;
import com.divinity.hlspells.world.blocks.OrbOfEnchantingBlock;
import com.divinity.hlspells.world.blocks.TentBlock;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.divinity.hlspells.world.blocks.blockentities.OrbOfEnchantingBE;
import com.divinity.hlspells.world.blocks.blockentities.TentBE;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, HLSpells.MODID);

    public static final DeferredHolder<Block, Block> CUSTOM_FROSTED_ICE = BLOCKS.register("frosted_ice", () ->
            new CustomFrostedIce(BlockBehaviour.Properties.ofFullCopy(Blocks.FROSTED_ICE)
                    .friction(0.98F)
                    .randomTicks()
                    .strength(0.5F)
                    .sound(SoundType.GLASS)
                    .noOcclusion()));

    public static final DeferredHolder<Block, Block> ALTAR_OF_ATTUNEMENT_BLOCK = BLOCKS.register("altar_of_attunement", () ->
            new AltarOfAttunementBlock((BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .requiresCorrectToolForDrops()
                    .lightLevel(level -> 3).strength(5.0F, 7.0F))));

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, HLSpells.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AltarOfAttunementBE>> ALTAR_BE = BLOCK_ENTITIES.register("altar_of_attunement", () -> BlockEntityType.Builder.of(AltarOfAttunementBE::new, ALTAR_OF_ATTUNEMENT_BLOCK.get()).build(null));

    public static final DeferredHolder<Block, Block> ORB_OF_ENCHANTING = BLOCKS.register("orb_of_enchanting",
            ()-> new OrbOfEnchantingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).strength(0.5f)
                    .requiresCorrectToolForDrops()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OrbOfEnchantingBE>> ORB_BE = BLOCK_ENTITIES.register("orb_of_enchanting", () -> BlockEntityType.Builder.of(OrbOfEnchantingBE::new, ORB_OF_ENCHANTING.get()).build(null));

    public static final DeferredHolder<Block, Block> ORANGE_TENT = BLOCKS.register("orange_tent",
            ()-> new TentBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).strength(1.0f)
                    .requiresCorrectToolForDrops()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TentBE>> ORANGE_TENT_BE = BLOCK_ENTITIES.register("orange_tent", () -> BlockEntityType.Builder.of(TentBE::new, ORANGE_TENT.get()).build(null));
}
