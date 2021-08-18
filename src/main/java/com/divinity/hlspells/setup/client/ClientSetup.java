package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.renderers.BaseBoltRenderer;
import com.divinity.hlspells.renderers.StormBoltRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static boolean frameTwo = false;
    public static boolean frameThree = false;
    public static boolean wandFrameThree = false;

    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), (stack, world, living) -> {

                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem().getItem() instanceof SpellBookItem) {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        frameTwo = true;
                        return 0.4F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        frameThree = true;
                        return 0.6F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.8F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71988) {
                        return 1;
                    }
                }
                return 0;
            });

            ItemModelsProperties.register(ItemInit.WAND.get(), new ResourceLocation("pull"), (stack, world, living) ->
            {
                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem().getItem() instanceof WandItem) {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        return 0.4F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        wandFrameThree = true;
                        return 0.6F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988) {
                        return 0.8F;
                    } else if ((double) living.getUseItemRemainingTicks() < 71988) {
                        return 1;
                    }
                }
                return 0;
            });
        });
        ClientRegistry.registerKeyBinding(HLSpells.WAND_BINDING);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.STORM_BULLET_ENTITY.get(), StormBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.PIERCING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/green_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.FLAMING_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AQUA_BOLT_ENTITY.get(), manager -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
    }
}