package com.divinity.hlspells.events;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.langproviders.EnUsLangProvider;
import com.divinity.hlspells.loot.SetSpell;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.divinity.hlspells.HLSpells.LOGGER;
import static net.minecraft.core.registries.BuiltInRegistries.LOOT_FUNCTION_TYPE;


@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class ModEventHandler {

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
    public static void onGatherData(GatherDataEvent event) {
        LOGGER.info("Gathering data providers!");
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(event.includeServer(), new EnUsLangProvider(generator.getPackOutput()));
        }
    }

    @SuppressWarnings("all")
    private static LootItemFunctionType register(String id, LootItemConditionalFunction.Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(LOOT_FUNCTION_TYPE, ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, id), new LootItemFunctionType(serializer));
    }

}
