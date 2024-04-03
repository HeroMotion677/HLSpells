package com.divinity.hlspells.setup.init;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ConfigData {

    public final ForgeConfigSpec.BooleanValue spellsUseXP;
    public final ForgeConfigSpec.BooleanValue lootOnlyMode;
    public final ForgeConfigSpec.BooleanValue summonsAttackPlayers;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireMobsList;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> sapientMobsList;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> lightningSpellList;


    public ConfigData(ForgeConfigSpec.Builder builder) {
        builder.push("HLSpells Config");
        spellsUseXP = builder.comment("Should the spells use experience?").define("spellsUseXP", true);
        lootOnlyMode = builder.comment("Should the spells only be obtainable from loot?").define("lootOnlyMode", false);
        summonsAttackPlayers = builder.comment("Should summoned entities from spells be able to attack players?").define("summonsAttackPlayers", true);
        ArrayList<String> defaultFireMobsList = Lists.newArrayList("minecraft:blaze", "minecraft:magma_cube", "minecraft:enderman");
        fireMobsList = builder.comment("List of mobs which should take extra damage from aqua bolt")
                .defineList("fireMobsList", defaultFireMobsList, String.class::isInstance);
        ArrayList<String> defaultSapientMobsList = Lists.newArrayList("minecraft:villager", "minecraft:pillager",
                "minecraft:evoker", "minecraft:illusioner", "minecraft:vindicator", "minecraft:piglin_brute", "minecraft:piglin");
        lightningSpellList = builder.comment("List of mobs which do not get damaged by the Lightning III spell")
                .defineList("lightningSpellList", Lists.newArrayList(), String.class::isInstance);
        sapientMobsList = builder.comment("Lists of mobs which are immune to repel and lure spell")
                .defineList("sapientMobsList", defaultSapientMobsList, String.class::isInstance);
        builder.pop();
    }
}
