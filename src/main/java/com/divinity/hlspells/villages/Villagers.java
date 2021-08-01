package com.divinity.hlspells.villages;

import com.divinity.hlspells.HLSpells;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.function.Supplier;

public class Villagers {

    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, HLSpells.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, HLSpells.MODID);
    public static final RegistryObject<PointOfInterestType> MAGE_POI = POI.register("mage",
            () -> new PointOfInterestType("mage", getAllStates(Blocks.ENCHANTING_TABLE), 1, 1));
    public static final RegistryObject<VillagerProfession> MAGE = registerProfession("mage", Villagers.MAGE_POI);

    @SuppressWarnings("SameParameterValue")
    private static RegistryObject<VillagerProfession> registerProfession(String name, Supplier<PointOfInterestType> poiType) {
        return PROFESSIONS.register(name, () -> new VillagerProfessions(HLSpells.MODID + ":" + name, poiType.get(), ImmutableSet.of(),
                ImmutableSet.of(), () -> SoundEvents.ENCHANTMENT_TABLE_USE));
    }

    private static Set<BlockState> getAllStates(Block block) {
        return ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
    }
}
