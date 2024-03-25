package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticlesInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, HLSpells.MODID);

    public static final RegistryObject<SimpleParticleType> GREEN_PARTICLE=
            PARTICLE_TYPES.register("green_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLACK_PARTICLE=
            PARTICLE_TYPES.register("black_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLUE_PARTICLE =
            PARTICLE_TYPES.register("blue_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ORANGE_PARTICLE =
            PARTICLE_TYPES.register("orange_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PURPLE_PARTICLE =
            PARTICLE_TYPES.register("purple_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RED_PARTICLE =
            PARTICLE_TYPES.register("red_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WHITE_PARTICLE =
            PARTICLE_TYPES.register("white_particle", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_PARTICLE =
            PARTICLE_TYPES.register("yellow_particle", ()-> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> GREEN_PARTICLE_SMALL=
            PARTICLE_TYPES.register("green_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLACK_PARTICLE_SMALL=
            PARTICLE_TYPES.register("black_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLUE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("blue_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> ORANGE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("orange_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> PURPLE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("purple_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> RED_PARTICLE_SMALL=
            PARTICLE_TYPES.register("red_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> WHITE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("white_particle_small", ()-> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> YELLOW_PARTICLE_SMALL=
            PARTICLE_TYPES.register("yellow_particle_small", ()-> new SimpleParticleType(true));

    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }
}
