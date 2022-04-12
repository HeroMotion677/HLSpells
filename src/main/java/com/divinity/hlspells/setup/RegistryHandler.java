package com.divinity.hlspells.setup;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.*;
import com.divinity.hlspells.loot.EvokerLootModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    public static void init() {
        EnchantmentInit.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellInit.SPELLS_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityInit.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new EvokerLootModifier.Serializer().setRegistryName(new ResourceLocation(HLSpells.MODID, "evoker_modifier")));
    }
}