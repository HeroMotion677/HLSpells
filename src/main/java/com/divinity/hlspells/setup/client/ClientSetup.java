package com.divinity.hlspells.setup.client;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EntityInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.renderers.BaseBoltRenderer;
import com.divinity.hlspells.renderers.StormBoltRenderer;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{
    public static final SoundEvent pageTurnSound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.book.page_turn"));
    public static final SoundEvent evokerPrepareAttack = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.evoker.prepare_attack"));
    public static final SoundEvent evokerPrepareSummon = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.evoker.prepare_summon"));

    public static boolean frameTwo = false;
    public static boolean frameThree = false;

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

                    else if ((double) living.getUseItemRemainingTicks()  < 71997 && (double) living.getUseItemRemainingTicks() >= 71994)
                    {
                        frameTwo = true;
                        return 0.4F;
                    }

                    else if ((double) living.getUseItemRemainingTicks() < 71994 && (double) living.getUseItemRemainingTicks() >= 71991)
                    {
                        frameThree = true;
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
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.STORM_BULLET_ENTITY.get(), StormBoltRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.PIERCING_BOLT_ENTITY.get(), (manager) -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID,"textures/entity/bolt/green_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.FLAMING_BOLT_ENTITY.get(), (manager) -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/orange_bolt.png")));
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.AQUA_BOLT_ENTITY.get(), (manager) -> new BaseBoltRenderer<>(manager, new ResourceLocation(HLSpells.MODID, "textures/entity/bolt/blue_bolt.png")));
    }
}