package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.living.summoned.SummonedSkeletonEntity;
import com.divinity.hlspells.entities.living.summoned.SummonedVexEntity;
import com.divinity.hlspells.entities.projectile.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HLSpells.MODID);

    public static final RegistryObject<EntityType<InvisibleTargetingEntity>> INVISIBLE_TARGETING_ENTITY = ENTITIES.register("invisible_targeting_entity", () ->
            EntityType.Builder.of(InvisibleTargetingEntity::new, MobCategory.MISC).build(HLSpells.MODID + "invisible_targeting_entity"));

    public static final RegistryObject<EntityType<KnockbackBoltEntity>> KNOCKBACK_BOLT_ENTITY = ENTITIES.register("knockback_bolt", () ->
            EntityType.Builder.of(KnockbackBoltEntity::new, MobCategory.MISC).build(HLSpells.MODID + "knockback_bolt"));

    public static final RegistryObject<EntityType<PiercingBoltEntity>> PIERCING_BOLT_ENTITY = ENTITIES.register("piercing_bolt", () ->
            EntityType.Builder.of(PiercingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "piercing_bolt"));

    public static final RegistryObject<EntityType<FlamingBoltEntity>> FLAMING_BOLT_ENTITY = ENTITIES.register("flaming_bolt", () ->
            EntityType.Builder.of(FlamingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "flaming_bolt"));

    public static final RegistryObject<EntityType<AquaBoltEntity>> AQUA_BOLT_ENTITY = ENTITIES.register("aqua_bolt", () ->
            EntityType.Builder.of(AquaBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "aqua_bolt"));

    public static final RegistryObject<EntityType<FreezingBoltEntity>> FREEZING_BOLT_ENTITY = ENTITIES.register("freezing_bolt", () ->
            EntityType.Builder.of(FreezingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "freezing_bolt"));

    public static final RegistryObject<EntityType<ChorusBoltEntity>> CHORUS_BOLT_ENTITY = ENTITIES.register("chorus_bolt", () ->
            EntityType.Builder.of(ChorusBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "chorus_bolt"));

    public static final RegistryObject<EntityType<SummonedVexEntity>> SUMMONED_VEX_ENTITY = ENTITIES.register("summoned_vex", () ->
            EntityType.Builder.of(SummonedVexEntity::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F)
                    .clientTrackingRange(8).build(HLSpells.MODID + "summoned_vex"));

    public static final RegistryObject<EntityType<SummonedSkeletonEntity>> SUMMONED_SKELETON_ENTITY = ENTITIES.register("summoned_skeleton", () ->
            EntityType.Builder.of(SummonedSkeletonEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F)
                    .clientTrackingRange(8).build(HLSpells.MODID + "summoned_skeleton"));
}
