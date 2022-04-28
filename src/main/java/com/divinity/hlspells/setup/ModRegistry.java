package com.divinity.hlspells.setup;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.*;
import com.divinity.hlspells.loot.EvokerLootModifier;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistry {
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EnchantmentInit.ENCHANTMENTS.register(bus);
        ItemInit.ITEMS.register(bus);
        SpellInit.SPELLS_DEFERRED_REGISTER.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        MenuTypeInit.MENUS.register(bus);
        EntityInit.ENTITIES.register(bus);
    }

    @SubscribeEvent
    public static void registerModifierSerializers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new EvokerLootModifier.Serializer().setRegistryName(new ResourceLocation(HLSpells.MODID, "evoker_modifier")));
    }
}