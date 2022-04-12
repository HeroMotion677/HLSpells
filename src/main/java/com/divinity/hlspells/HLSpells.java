package com.divinity.hlspells;

import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.init.ConfigData;
import com.divinity.hlspells.setup.RegistryHandler;
import com.divinity.hlspells.setup.client.ClientSetup;
import com.divinity.hlspells.villages.StructureGen;
import com.divinity.hlspells.villages.Villagers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.targets.FMLServerUserdevLaunchHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(HLSpells.MODID)
public class HLSpells {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "hlspells";
    public static final ConfigData CONFIG;
    public static boolean isCurioLoaded = false;
    private static final ForgeConfigSpec CONFIG_SPEC;

    static {
        final Pair<ConfigData, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ConfigData::new);
        CONFIG = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    public HLSpells() {
        // Init the RegistryHandler class
        RegistryHandler.init();
        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendImc);
        // Only registers client setup on client only
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init));
        registerAllDeferredRegistryObjects(FMLJavaModLoadingContext.get().getModEventBus());
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.addListener(this::setupMageHouses);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        isCurioLoaded = ModList.get().isLoaded("curios");
    }

    private void registerAllDeferredRegistryObjects(IEventBus modBus) {
        Villagers.POI.register(modBus);
        Villagers.PROFESSIONS.register(modBus);
    }

    public void setupMageHouses(final ServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    public void sendImc(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios"))
            CuriosCompat.sendImc();
    }
}