package com.divinity.hlspells.setup;

import com.divinity.hlspells.init.*;
import com.divinity.hlspells.items.WandItem;
import com.divinity.hlspells.items.capabilities.IWandCap;
import com.divinity.hlspells.items.capabilities.WandCap;
import com.divinity.hlspells.items.capabilities.WandItemStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler
{
    public static void init()
    {
        EnchantmentInit.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellInit.SPELLS_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellBookInit.SPELL_BOOK_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        EntityInit.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}