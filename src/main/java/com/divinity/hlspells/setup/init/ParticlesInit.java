package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ParticlesInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, HLSpells.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_PARTICLE=
            PARTICLE_TYPES.register("green_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLACK_PARTICLE=
            PARTICLE_TYPES.register("black_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_PARTICLE =
            PARTICLE_TYPES.register("blue_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORANGE_PARTICLE =
            PARTICLE_TYPES.register("orange_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURPLE_PARTICLE =
            PARTICLE_TYPES.register("purple_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_PARTICLE =
            PARTICLE_TYPES.register("red_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITE_PARTICLE =
            PARTICLE_TYPES.register("white_particle", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_PARTICLE =
            PARTICLE_TYPES.register("yellow_particle", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_PARTICLE_SMALL=
            PARTICLE_TYPES.register("green_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLACK_PARTICLE_SMALL=
            PARTICLE_TYPES.register("black_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("blue_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORANGE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("orange_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURPLE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("purple_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RED_PARTICLE_SMALL=
            PARTICLE_TYPES.register("red_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITE_PARTICLE_SMALL=
            PARTICLE_TYPES.register("white_particle_small", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_PARTICLE_SMALL=
            PARTICLE_TYPES.register("yellow_particle_small", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ORANGE_BOLT_BOOM=
            PARTICLE_TYPES.register("orange_bolt_boom", ()-> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> YELLOW_BOLT_BOOM=
            PARTICLE_TYPES.register("yellow_bolt_boom", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITE_BOLT_BOOM=
            PARTICLE_TYPES.register("white_bolt_boom", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLUE_BOLT_BOOM=
            PARTICLE_TYPES.register("blue_bolt_boom", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLACK_BOLT_BOOM=
            PARTICLE_TYPES.register("black_bolt_boom", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PURPLE_BOLT_BOOM=
            PARTICLE_TYPES.register("purple_bolt_boom", ()-> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GREEN_BOLT_BOOM=
            PARTICLE_TYPES.register("green_bolt_boom", ()-> new SimpleParticleType(true));

}
