package com.divinity.hlspells;

import com.divinity.hlspells.items.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.items.capabilities.totemcap.TotemCap;
import com.divinity.hlspells.items.capabilities.totemcap.TotemItemStorage;
import com.divinity.hlspells.items.capabilities.wandcap.IWandCap;
import com.divinity.hlspells.items.capabilities.wandcap.WandCap;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemStorage;
import com.divinity.hlspells.setup.RegistryHandler;
import com.divinity.hlspells.villages.POIFixup;
import com.divinity.hlspells.villages.StructureGen;
import com.divinity.hlspells.villages.Villagers;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HLSpells.MODID)
public class HLSpells {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    // Register the modid
    public static final String MODID = "hlspells";
    public static final KeyBinding WAND_BINDING = new KeyBinding("Wand Cycle", KeyConflictContext.UNIVERSAL, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_G, "HLSpells");

    public HLSpells() {
        // Init the RegistryHandler class
        RegistryHandler.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
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
    public void setupMageHouses(final FMLServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    public void setup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IWandCap.class, new WandItemStorage(), WandCap::new);
        CapabilityManager.INSTANCE.register(ITotemCap.class, new TotemItemStorage(), TotemCap::new);
        POIFixup.registerPOI();
    }
}