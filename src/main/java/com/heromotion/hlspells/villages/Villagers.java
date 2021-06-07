package com.heromotion.hlspells.villages;

import com.google.common.collect.ImmutableSet;

import com.heromotion.hlspells.HLSpells;

import net.minecraft.block.*;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.*;
import net.minecraft.village.PointOfInterestType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;
import java.util.Set;

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
