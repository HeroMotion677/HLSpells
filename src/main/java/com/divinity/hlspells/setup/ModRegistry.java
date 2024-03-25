package com.divinity.hlspells.setup;

import com.divinity.hlspells.setup.init.ParticlesInit;
import com.divinity.hlspells.setup.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModRegistry {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ParticlesInit.PARTICLE_TYPES.register(bus);
        EntityInit.ENTITIES.register(bus);
        EnchantmentInit.ENCHANTMENTS.register(bus);
        ItemInit.ITEMS.register(bus);
        SpellInit.SPELLS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        MenuTypeInit.MENUS.register(bus);
        VillagerInit.POI.register(bus);
        SoundInit.SOUNDS.register(bus);
        VillagerInit.PROFESSIONS.register(bus);

    }
}