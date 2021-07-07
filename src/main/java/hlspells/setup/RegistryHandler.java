package com.heromotion.hlspells.setup;

import com.heromotion.hlspells.init.EnchantmentInit;
import com.heromotion.hlspells.init.ItemInit;
import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.init.SpellInit;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler
{
    public static void init()
    {
        EnchantmentInit.ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ItemInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellBookInit.SPELL_BOOK_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        SpellInit.SPELLS_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}