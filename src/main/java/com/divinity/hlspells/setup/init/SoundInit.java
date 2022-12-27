package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HLSpells.MODID);

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(HLSpells.MODID, name)));
    }

    public static final RegistryObject<SoundEvent> ALTAR_TRANSFER = registerSoundEvent("altar_transfer");
    public static final RegistryObject<SoundEvent> CAST_BOLT = registerSoundEvent("cast_bolt");
    public static final RegistryObject<SoundEvent> CAST_FLAME = registerSoundEvent("cast_flame");
    public static final RegistryObject<SoundEvent> CAST_ICE = registerSoundEvent("cast_ice");
    public static final RegistryObject<SoundEvent> CAST_NECROMANCY = registerSoundEvent("cast_necromancy");
    public static final RegistryObject<SoundEvent> CHARGE_COMBAT = registerSoundEvent("charge_combat");
    public static final RegistryObject<SoundEvent> CHARGE_UTILITY = registerSoundEvent("charge_utility");
    public static final RegistryObject<SoundEvent> HELD_COMBAT = registerSoundEvent("held_combat");
    public static final RegistryObject<SoundEvent> HELD_ILLUMINATE = registerSoundEvent("held_illuminate");
    public static final RegistryObject<SoundEvent> HELD_UTILITY = registerSoundEvent("held_utility");
    public static final RegistryObject<SoundEvent> MISCAST_SOUND = registerSoundEvent("miscast_sound");
    public static final RegistryObject<SoundEvent> SPELL_ATTUNEMENT = registerSoundEvent("spell_attunement");
}
