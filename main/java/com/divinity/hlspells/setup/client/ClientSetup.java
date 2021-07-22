package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.StormBulletEntity;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.renderers.StormBulletEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    @SubscribeEvent
    public static void init (final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), (stack, world, living) -> {

                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem().getItem() instanceof SpellBookItem)
                {
                    if ((double) living.getUseItemRemainingTicks() < 72000 && (double) living.getUseItemRemainingTicks() >= 71997) {
                        return 0.2F;
                    }

                    else if ((double) living.getUseItemRemainingTicks()  < 71997 && (double) living.getUseItemRemainingTicks() >= 71994) {
                        return 0.4F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991) {
                        return 0.6F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71991 && (double) living.getUseItemRemainingTicks() >= 71988)
                    {
                        return 0.8F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71988)
                    {
                        return 1;
                    }
                }
                return 0;
            });
        });
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.STORM_BULLET_ENTITY.get(), StormBulletEntityRenderer::new);
    }
}