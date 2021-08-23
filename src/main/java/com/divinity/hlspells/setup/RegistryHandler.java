package com.divinity.hlspells.setup;

import com.divinity.hlspells.init.*;
import com.divinity.hlspells.loot.LootTableHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {
    public static void init() {
        EnchantmentInit.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlockInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellInit.SPELLS_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityInit.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LootTableHandler.LootFunctions.init();
    }
}