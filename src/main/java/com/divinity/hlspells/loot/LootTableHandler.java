package com.divinity.hlspells.loot;

import com.divinity.hlspells.HLSpells;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class LootTableHandler {

    @SubscribeEvent
    public static void registerLoot(LootTableLoadEvent evt) {
        String prefix = "minecraft:chests/";
        String name = evt.getName().toString();
        if (name.startsWith(prefix)) {
            String file = name.substring(name.indexOf(prefix) + prefix.length());
            switch (file) {
                case "woodland_mansion":
                case "end_city_treasure":
                case "stronghold_library":
                case "jungle_temple":
                case "simple_dungeon":
                case "desert_pyramid":
                case "nether_bridge":
                case "bastion_treasure":
                    evt.getTable().addPool(getInjectPool(file));
                    break;
                default:
                    break;
            }
        }
    }

    public static LootPool getInjectPool(String entryName) {
        return LootPool.lootPool()
                .add(getInjectEntry(entryName))
                .setBonusRolls(BinomialDistributionGenerator.binomial(0, 1))
                .name("inject")
                .build();
    }

    private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(HLSpells.MODID, "inject/" + name);
        return LootTableReference.lootTableReference(table)
                .setWeight(1);
    }
}
