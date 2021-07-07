package com.heromotion.hlspells.setup.client;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.ItemInit;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        // Make this deferred for unsafe threads
        event.enqueueWork(() -> {
            // Item Properties
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("using"), ((p_call_1_, p_call_2_, p_call_3_) -> {
                return p_call_3_ != null && p_call_3_.isUsingItem() && p_call_3_.getUseItem() == p_call_1_ ? 1.0F : 0.0F;
            }));
            ItemModelsProperties.register(ItemInit.SPELL_BOOK.get(), new ResourceLocation("use"), ((p_call_1_, p_call_2_, p_call_3_) -> {
                if (p_call_3_ == null) {
                    return 0.0F;
                } else {
                    return p_call_3_.getUseItem() != p_call_1_ ? 0.0F : (p_call_1_.getUseDuration() - p_call_3_.getUseItemRemainingTicks() / 20.0F);
                }
            }));
        });
    }
}