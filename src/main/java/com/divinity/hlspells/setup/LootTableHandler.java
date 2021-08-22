package com.divinity.hlspells.setup;

import com.divinity.hlspells.HLSpells;
import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class LootTableHandler {
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
                .bonusRolls(0, 1)
                .name("inject")
                .build();
    }

    private static LootEntry.Builder<?> getInjectEntry(String name) {
        ResourceLocation table = new ResourceLocation(HLSpells.MODID, "inject/" + name);
        return TableLootEntry.lootTableReference(table)
                .setWeight(1);
    }
}
