package com.divinity.hlspells.loot;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventLoot {
    public static LootItemFunctionType SET_SPELL;

    @SubscribeEvent
    public static void commonSetup (FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            SET_SPELL = register("set_spell", new SetSpell.Serializer());
        });
    }

    private static LootItemFunctionType register(String id, LootItemConditionalFunction.Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(HLSpells.MODID, id), new LootItemFunctionType(serializer));
    }
}
