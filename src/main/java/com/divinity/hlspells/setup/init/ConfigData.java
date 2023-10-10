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
    public final ForgeConfigSpec.DoubleValue spellCastTime;
    public final ForgeConfigSpec.DoubleValue teleportRange;
    public final ForgeConfigSpec.DoubleValue woodStaffCastTime;
    public final ForgeConfigSpec.DoubleValue goldStaffCastTime;
    public final ForgeConfigSpec.DoubleValue netheriteStaffCastTime;
    public final ForgeConfigSpec.DoubleValue cooldownDuration;

    public ConfigData(ForgeConfigSpec.Builder builder) {
        builder.push("HLSpells Config");
        spellsUseXP = builder.comment("Should the spells use experience?").define("spellsUseXP", true);
        lootOnlyMode = builder.comment("Should the spells only be obtainable from loot?").define("lootOnlyMode", false);
        summonsAttackPlayers = builder.comment("Should summoned entities from spells be able to attack players?").define("summonsAttackPlayers", true);
        ArrayList<String> defaultFireMobsList = Lists.newArrayList("minecraft:blaze", "minecraft:magma_cube");
        fireMobsList = builder.comment("List of mobs which should take extra damage from aqua bolt")
                .defineList("fireMobsList", defaultFireMobsList, String.class::isInstance);
        ArrayList<String> defaultSapientMobsList = Lists.newArrayList("minecraft:villager", "minecraft:pillager",
                "minecraft:evoker", "minecraft:illusioner", "minecraft:vindicator", "minecraft:piglin_brute", "minecraft:piglin");
        lightningSpellList = builder.comment("List of mobs which do not get damaged by the Lightning III spell")
                .defineList("lightningSpellList", Lists.newArrayList(), String.class::isInstance);
        sapientMobsList = builder.comment("Lists of mobs which are immune to repel and lure spell")
                .defineList("sapientMobsList", defaultSapientMobsList, String.class::isInstance);
        spellCastTime = builder.comment("How long should spell items be held before they cast? (seconds)")
                .defineInRange("spellCastTime", 0.3, 0, 60);
        teleportRange = builder.comment("Teleport range for the teleport spell.").defineInRange("teleportRange", 50D, 1D, 500D);
        woodStaffCastTime = builder.comment("How long should the Wooden Staff be held before it casts? (seconds)")
                .defineInRange("woodStaffCastTime", 0.6, 0, 60);
        goldStaffCastTime = builder.comment("How long should the Gold Staff be held before it casts? (seconds)")
                .defineInRange("goldStaffCastTime", 0.2, 0, 60);
        netheriteStaffCastTime = builder.comment("How long should the Netherite Staff be held before it casts? (seconds)")
                .defineInRange("netheriteStaffCastTime", 0.4, 0, 60);
        cooldownDuration = builder.comment("How long should should the cooldown of spell items be when you cast? (seconds)")
                .defineInRange("cooldownDuration", 1.2, 0, 60);
        builder.pop();
    }
}
