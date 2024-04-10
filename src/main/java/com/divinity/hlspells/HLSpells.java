package com.divinity.hlspells;

import com.divinity.hlspells.capabilities.playercap.IPlayerCap;
import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.totemcap.ITotemCap;
import com.divinity.hlspells.client.models.BaseBoltModel;
import com.divinity.hlspells.client.models.FireballModel;
import com.divinity.hlspells.client.models.WizardHatModel;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.events.ModelLayers;
import com.divinity.hlspells.setup.ModRegistry;
import com.divinity.hlspells.setup.init.ConfigData;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.VillagerInit;
import com.divinity.hlspells.world.structures.villages.StructureGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.divinity.hlspells.events.ForgeClientEventHandler.hatArmorModel;

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
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // Init the RegistryHandler class
        ModRegistry.init();
        // Registers an event with the mod specific event bus. This is needed to register new stuff.
        bus.addListener(this::sendImc);
        bus.addListener(this::registerAllCapabilities);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.addListener(this::setupMageHouses);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        isCurioLoaded = ModList.get().isLoaded("curios");

        bus.addListener(this::commonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(this::registerLayer);
            bus.addListener(this::registerModelLayers);
        });
    }

    private void setupMageHouses(final ServerAboutToStartEvent event) {
        StructureGen.setupVillageWorldGen(event.getServer().registryAccess());
    }

    @OnlyIn(Dist.CLIENT)
    public void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLayers.WIZARD_HAT_LAYER, WizardHatModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.BOLT_LAYER, BaseBoltModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.FIRE_BALL_LAYER, FireballModel::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerModelLayers(EntityRenderersEvent.AddLayers event) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(
                Minecraft.getInstance().getEntityRenderDispatcher(),
                Minecraft.getInstance().getItemRenderer(),
                Minecraft.getInstance().getBlockRenderer(),
                Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer(),
                Minecraft.getInstance().getResourceManager(),
                Minecraft.getInstance().getEntityModels(),
                Minecraft.getInstance().font);
        var wizardHatArmorModel = new WizardHatModel<>(context.bakeLayer(ModelLayers.WIZARD_HAT_LAYER));
        hatArmorModel.put(ItemInit.WIZARD_HAT.get(), wizardHatArmorModel);
    }

    private void registerAllCapabilities(final RegisterCapabilitiesEvent event) {
        event.register(IPlayerCap.class);
        event.register(ISpellHolder.class);
        event.register(ITotemCap.class);
    }
	
    private void sendImc(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.sendImc();
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(()-> {
            VillagerInit.registerPOIs();
        });
    }
}