package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.loot.EvokerLootModifier;
import com.divinity.hlspells.loot.SetSpell;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class LootInit {

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, HLSpells.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<EvokerLootModifier>> EVOKER_LOOT =
            LOOT_MODIFIER_SERIALIZERS.register("evoker_modifier", () -> EvokerLootModifier.CODEC);

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, HLSpells.MODID);

    public static final DeferredHolder<LootItemFunctionType<?>, LootItemFunctionType<SetSpell>> SET_SPELL =
            LOOT_FUNCTIONS.register("set_spell", () -> new LootItemFunctionType<>(SetSpell.CODEC));
}
