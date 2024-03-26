package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class VillagerInit {

    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, HLSpells.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, HLSpells.MODID);

    public static final RegistryObject<PoiType> MAGE_POI = POI.register("mage", () -> new PoiType("mage", getAllStates(), 1, 1));
    public static final RegistryObject<VillagerProfession> MAGE = PROFESSIONS.register("mage", () ->
            new VillagerProfession(HLSpells.MODID + ":" + "mage", MAGE_POI.get(), ImmutableSet.of(),
                    ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));

    private static Set<BlockState> getAllStates() {
        return ImmutableSet.copyOf(BlockInit.ORB_OF_ENCHANTING.get().getStateDefinition().getPossibleStates());
    }
}
