package com.heromotion.hlspells.setup.client;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.ItemInit;
import com.heromotion.hlspells.items.SpellBookItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup
{

    public static void init (final FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), (stack, world, living) -> {

                if (living instanceof PlayerEntity && living.isUsingItem() && living.getUseItem().getItem() instanceof SpellBookItem)
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
}