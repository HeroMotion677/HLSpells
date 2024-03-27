package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.loot.EvokerLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LootInit {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, HLSpells.MODID);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> EVOKER_LOOT =
            LOOT_MODIFIER_SERIALIZERS.register("evoker_modifier", EvokerLootModifier.CODEC);
    public static void register(IEventBus bus){
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
