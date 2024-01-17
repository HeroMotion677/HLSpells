package com.divinity.hlspells.particle;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, HLSpells.MODID);

    public static final RegistryObject<SimpleParticleType> GREEN_PARTICLE =
            PARTICLE_TYPES.register("green_particle", ()-> new SimpleParticleType(true));
    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
