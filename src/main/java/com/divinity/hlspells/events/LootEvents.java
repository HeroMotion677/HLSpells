package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.loot.EvokerLootModifier;
import com.divinity.hlspells.loot.SetSpell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class LootEvents {

    public static LootItemFunctionType SET_SPELL;

    @SubscribeEvent
    public static void commonSetup (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // We need to initialize this here during CommonSetup because this event fires after the RegistryEvents have been fired
            // Initializing any sooner would result in the game crashing
            SET_SPELL = register("set_spell", new SetSpell.Serializer());
        });
    }

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(new EvokerLootModifier.Serializer().setRegistryName(new ResourceLocation(HLSpells.MODID, "evoker_modifier")));
    }

    @SuppressWarnings("all")
    private static LootItemFunctionType register(String id, LootItemConditionalFunction.Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(HLSpells.MODID, id), new LootItemFunctionType(serializer));
    }

    @Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.FORGE)
    public static class ForgeEvents {

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
            return LootPool.lootPool().add(getInjectEntry(entryName)).setBonusRolls(BinomialDistributionGenerator.binomial(0, 1)).name("inject").build();
        }

        private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name) {
            ResourceLocation table = new ResourceLocation(HLSpells.MODID, "inject/" + name);
            return LootTableReference.lootTableReference(table).setWeight(1);
        }
    }
}
