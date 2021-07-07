package com.heromotion.hlspells;

import com.heromotion.hlspells.init.ItemInit;
import com.heromotion.hlspells.events.spell.HeldSpells;

import com.heromotion.hlspells.setup.RegistryHandler;
import com.heromotion.hlspells.setup.client.ClientSetup;
import com.heromotion.hlspells.villages.POIFixup;
import com.heromotion.hlspells.villages.StructureGen;
import com.heromotion.hlspells.villages.Villagers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HLSpells.MODID)
public class HLSpells
{

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    // Register the modid
    public static final String MODID = "hlspells";

    public HLSpells()
    {

        // Init the RegistryHandler class
        RegistryHandler.init();

        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::spellBookModelRender);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        registerAllDeferredRegistryObjects(FMLJavaModLoadingContext.get().getModEventBus());

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.addListener(this::setupMageHouses);
        MinecraftForge.EVENT_BUS.register(new HeldSpells());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void registerAllDeferredRegistryObjects(IEventBus modBus)
    {
        Villagers.POI.register(modBus);
        Villagers.PROFESSIONS.register(modBus);
    }


    /*
      Add to Village pools in FMLServerAboutToStartEvent so Mage houses shows up in Villages modified by datapacks.
     */

    // Renders book model
    public void spellBookModelRender (final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), (stack, world, living) -> {

                if (living instanceof PlayerEntity && living.isUsingItem())
                {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71996) {
                        return 0.2F;
                    }

                    else if ((double) living.getUseItemRemainingTicks()  < 71996 && (double) living.getUseItemRemainingTicks() >= 71992) {
                        return 0.4F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71992 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.6F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71988 && (double) living.getUseItemRemainingTicks() >= 71984)
                    {
                        return 0.8F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71984)
                    {
                        return 1;
                    }
                }
                return 0;
            });
        });
    }

    public void setupMageHouses(final FMLServerAboutToStartEvent event)
    {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    public void setup(final FMLCommonSetupEvent event) {
        POIFixup.fixup();
    }
}