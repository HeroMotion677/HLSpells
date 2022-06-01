package com.divinity.hlspells.setup;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Bus.MOD)
public class ModRegistry {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EnchantmentInit.ENCHANTMENTS.register(bus);
        ItemInit.ITEMS.register(bus);
        SpellInit.SPELLS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        MenuTypeInit.MENUS.register(bus);
        EntityInit.ENTITIES.register(bus);
        VillagerInit.POI.register(bus);
        VillagerInit.PROFESSIONS.register(bus);
    }
}