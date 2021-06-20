package com.heromotion.hlspells;

import com.heromotion.hlspells.setup.RegistryHandler;
import com.heromotion.hlspells.setup.client.ClientSetup;
import com.heromotion.hlspells.villages.*;

import net.minecraftforge.common.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.*;

@Mod(HLSpells.MODID)
public class HLSpells {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Register the modid
    public static final String MODID = "hlspells";

    public HLSpells() {

        // Init the RegistryHandler class
        RegistryHandler.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        registerAllDeferredRegistryObjects(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.addListener(this::setupMageHouses);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerAllDeferredRegistryObjects(IEventBus modBus) {
        Villagers.POI.register(modBus);
        Villagers.PROFESSIONS.register(modBus);
    }

    /*
      Add to Village pools in FMLServerAboutToStartEvent so Mage houses shows up in Villages modified by datapacks.
     */
    public void setupMageHouses(FMLServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    public void setup(final FMLCommonSetupEvent event) {
        POIFixup.fixup();
    }
}
