package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HLSpells.MODID);

    public static final RegistryObject<SoundEvent> ALTAR_TRANSFER = SOUNDS.register("altar_transfer", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/altar_transfer.ogg")));
    public static final RegistryObject<SoundEvent> CAST_BOLT = SOUNDS.register("cast_bolt", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/cast_bolt.ogg")));
    public static final RegistryObject<SoundEvent> CAST_FLAME = SOUNDS.register("cast_flame", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/cast_flame.ogg")));
    public static final RegistryObject<SoundEvent> CAST_ICE = SOUNDS.register("cast_ice", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/cast_ice.ogg")));
    public static final RegistryObject<SoundEvent> CAST_NECROMANCY = SOUNDS.register("cast_necromancy", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/cast_necromancy.ogg")));
    public static final RegistryObject<SoundEvent> CHARGE_COMBAT = SOUNDS.register("charge_combat", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/charge_combat.ogg")));
    public static final RegistryObject<SoundEvent> CHARGE_UTILITY = SOUNDS.register("charge_utility", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/charge_utility.ogg")));
    public static final RegistryObject<SoundEvent> HELD_COMBAT = SOUNDS.register("held_combat", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/held_combat.ogg")));
    public static final RegistryObject<SoundEvent> HELD_ILLUMINATE = SOUNDS.register("held_illuminate", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/held_illuminate.ogg")));
    public static final RegistryObject<SoundEvent> HELD_UTILITY = SOUNDS.register("held_utility", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/held_utility.ogg")));
    public static final RegistryObject<SoundEvent> MISCAST_SOUND = SOUNDS.register("miscast_sound", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/miscast_sound.ogg")));
    public static final RegistryObject<SoundEvent> SPELL_ATTUNEMENT = SOUNDS.register("spell_attunement", () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, "sounds/spell_attunement.ogg")));
}
