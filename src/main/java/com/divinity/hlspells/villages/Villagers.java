package com.divinity.hlspells.villages;

import com.divinity.hlspells.HLSpells;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

public class Villagers {

    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, HLSpells.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, HLSpells.MODID);
    public static final RegistryObject<PointOfInterestType> MAGE_POI = POI.register("mage",
            () -> new PointOfInterestType("mage", getAllStates(), 1, 1));
    public static final RegistryObject<VillagerProfession> MAGE = PROFESSIONS.register("mage", () ->
            new VillagerProfession(HLSpells.MODID + ":" + "mage", MAGE_POI.get(), ImmutableSet.of(),
                    ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));

    private static Set<BlockState> getAllStates() {
        return ImmutableSet.copyOf(Blocks.ENCHANTING_TABLE.getStateDefinition().getPossibleStates());
    }
}
