package com.divinity.hlspells;

import com.divinity.hlspells.client.models.BaseBoltModel;
import com.divinity.hlspells.client.models.FireballModel;
import com.divinity.hlspells.client.models.WizardHatModel;
import com.divinity.hlspells.compat.CuriosCompat;
import com.divinity.hlspells.events.ModelLayers;
import com.divinity.hlspells.setup.ModRegistry;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.ConfigData;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.world.structures.villages.StructureGen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
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
    private static final ModConfigSpec CONFIG_SPEC;

    static {
        final Pair<ConfigData, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ConfigData::new);
        CONFIG = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }

    public HLSpells(IEventBus bus, ModContainer container) {
        ModRegistry.init(bus);
        bus.addListener(this::sendImc);
        bus.addListener(this::registerCapabilities);
        if (ModList.get().isLoaded("curios")) {
            bus.addListener(this::registerCuriosCapabilities);
        }

        NeoForge.EVENT_BUS.addListener(this::setupMageHouses);
        container.registerConfig(ModConfig.Type.COMMON, CONFIG_SPEC);
        isCurioLoaded = ModList.get().isLoaded("curios");

        if (FMLEnvironment.dist == Dist.CLIENT) {
            bus.addListener(this::registerLayer);
            bus.addListener(this::registerModelLayers);
        }
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

    private void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockInit.ALTAR_BE.get(), (be, side) -> be.itemHandler);
    }

    private void registerCuriosCapabilities(final RegisterCapabilitiesEvent event) {
        CuriosCompat.registerCapabilities(event, ItemInit.TOTEMS);
    }

    private void sendImc(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.sendImc();
        }
    }
}
