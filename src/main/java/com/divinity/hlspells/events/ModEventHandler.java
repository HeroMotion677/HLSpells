package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.langproviders.EnUsLangProvider;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static com.divinity.hlspells.HLSpells.LOGGER;

@EventBusSubscriber(modid = HLSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        LOGGER.info("Gathering data providers!");
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(event.includeServer(), new EnUsLangProvider(generator.getPackOutput()));
        }
    }
}
