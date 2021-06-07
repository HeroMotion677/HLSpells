package com.heromotion.hlspells.villages;

import com.heromotion.hlspells.HLSpells;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.jigsaw.*;

import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class StructureGen {

    private static final ResourceLocation mainMageHouse = new ResourceLocation(HLSpells.MODID, "villages/mage_house_plains");

    public static void setupVillageWorldGen(DynamicRegistries dynamicRegistries) {
        // Add Houses to Vanilla Villages.
        addMageHouseToVillageConfig(dynamicRegistries, "village/plains/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "village/savanna/houses", new ResourceLocation(HLSpells.MODID, "villages/mage_house_savanna"), 4);
        addMageHouseToVillageConfig(dynamicRegistries, "village/desert/houses", new ResourceLocation(HLSpells.MODID, "villages/mage_house_desert"), 4);
        addMageHouseToVillageConfig(dynamicRegistries, "village/taiga/houses", new ResourceLocation(HLSpells.MODID, "villages/mage_house_taiga"), 4);
        addMageHouseToVillageConfig(dynamicRegistries, "village/snowy/houses", new ResourceLocation(HLSpells.MODID, "villages/mage_house_snowy"), 4);

        // Add Houses to other mod's structures. (Make sure Houses piece Jigsaw Block's Name matches the other mod piece Jigsaw's Target Name.
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/badlands/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/birch/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/dark_forest/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/jungle/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/mountains/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/oak/houses", mainMageHouse, 4);
        addMageHouseToVillageConfig(dynamicRegistries, "repurposed_structures:village/swamp/houses", mainMageHouse, 4);
    }

    private static void addMageHouseToVillageConfig(DynamicRegistries dynamicRegistries, String villagePiece, ResourceLocation waystoneStructure, int weight) {
        LegacySingleJigsawPiece piece = JigsawPiece.legacy(waystoneStructure.toString()).apply(JigsawPattern.PlacementBehaviour.RIGID);
        JigsawPattern pool = dynamicRegistries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).getOptional(new ResourceLocation(villagePiece)).orElse(null);
        if (pool != null) {
            // Pretty sure this can be an immutable list (when datapacked) so gotta make a copy to be safe.
            List<JigsawPiece> listOfPieces = new ArrayList<>(pool.templates);
            for (int i = 0; i < weight; i++) {
                listOfPieces.add(piece);
            }
            pool.templates = listOfPieces;
        }
    }
}
