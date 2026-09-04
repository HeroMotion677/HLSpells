package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, HLSpells.MODID);

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, name)));
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> ALTAR_TRANSFER = registerSoundEvent("altar_transfer");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAST_BOLT = registerSoundEvent("cast_bolt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAST_FLAME = registerSoundEvent("cast_flame");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAST_ICE = registerSoundEvent("cast_ice");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAST_NECROMANCY = registerSoundEvent("cast_necromancy");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHARGE_COMBAT = registerSoundEvent("charge_combat");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHARGE_UTILITY = registerSoundEvent("charge_utility");
    public static final DeferredHolder<SoundEvent, SoundEvent> HELD_COMBAT = registerSoundEvent("held_combat");
    public static final DeferredHolder<SoundEvent, SoundEvent> HELD_ILLUMINATE = registerSoundEvent("held_illuminate");
    public static final DeferredHolder<SoundEvent, SoundEvent> HELD_UTILITY = registerSoundEvent("held_utility");
    public static final DeferredHolder<SoundEvent, SoundEvent> MISCAST_SOUND = registerSoundEvent("miscast_sound");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAST_SOUND = registerSoundEvent("cast_sound");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPELL_ATTUNEMENT = registerSoundEvent("spell_attunement");
}
