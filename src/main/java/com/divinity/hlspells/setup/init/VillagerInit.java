package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VillagerInit {

    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, HLSpells.MODID);
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, HLSpells.MODID);

    public static final DeferredHolder<PoiType, PoiType> MAGE_POI = POI.register("mage",
            () -> new PoiType(ImmutableSet.copyOf(BlockInit.ORB_OF_ENCHANTING.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> MAGE = PROFESSIONS.register("mage",
            () -> new VillagerProfession("mage", x -> x.value() == MAGE_POI.get(),
                    x -> x.value() == MAGE_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));
}
