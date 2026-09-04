package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.living.summoned.SummonedWitherSkeletonEntity;
import com.divinity.hlspells.entities.living.summoned.SummonedVexEntity;
import com.divinity.hlspells.entities.projectile.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = HLSpells.MODID)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, HLSpells.MODID);
    private static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final DeferredHolder<EntityType<?>, EntityType<MysticBoltEntity>> MYSTIC_BOLT_ENTITY = registerEntity("mystic_bolt", () -> EntityType.Builder.of(MysticBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<InvisibleTargetingEntity>> INVISIBLE_TARGETING_ENTITY = registerEntity("invisible_targeting_entity", () -> EntityType.Builder.of(InvisibleTargetingEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<KnockbackBoltEntity>> KNOCKBACK_BOLT_ENTITY = registerEntity("knockback_bolt", () -> EntityType.Builder.of(KnockbackBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<PiercingBoltEntity>> PIERCING_BOLT_ENTITY = registerEntity("piercing_bolt", () -> EntityType.Builder.<PiercingBoltEntity>of(PiercingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<FlamingBoltEntity>> FLAMING_BOLT_ENTITY = registerEntity("flaming_bolt", () -> EntityType.Builder.of(FlamingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<FlamingBreathEntity>> FLAMING_BREATH_ENTITY = registerEntity("flaming_breath", () -> EntityType.Builder.of(FlamingBreathEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<FreezingBreathEntity>> FREEZING_BREATH_ENTITY = registerEntity("freezing_breath", () -> EntityType.Builder.of(FreezingBreathEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<WitherBreathEntity>> WITHER_BREATH_ENTITY = registerEntity("wither_breath", () -> EntityType.Builder.of(WitherBreathEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<FireballEntity>> FIREBALL = registerEntity("fireball", () -> EntityType.Builder.of(FireballEntity::new, MobCategory.MISC).sized(0.5125F, 0.5125F));
    public static final DeferredHolder<EntityType<?>, EntityType<Fireball2Entity>> FIREBALL2 = registerEntity("fireball2", () -> EntityType.Builder.of(Fireball2Entity::new, MobCategory.MISC).sized(0.7125F, 0.7125F));
    public static final DeferredHolder<EntityType<?>, EntityType<AquaBoltEntity>> AQUA_BOLT_ENTITY = registerEntity("aqua_bolt", () -> EntityType.Builder.of(AquaBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<FreezingBoltEntity>> FREEZING_BOLT_ENTITY = registerEntity("freezing_bolt", () -> EntityType.Builder.of(FreezingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<ChorusBoltEntity>> CHORUS_BOLT_ENTITY = registerEntity("chorus_bolt", () -> EntityType.Builder.of(ChorusBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final DeferredHolder<EntityType<?>, EntityType<WitherSkullEntity>> WITHER_SKULL_ENTITY = registerEntity("wither_skull", () -> EntityType.Builder.of(WitherSkullEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedVexEntity>> SUMMONED_VEX_ENTITY = registerEntity("summoned_vex", () -> EntityType.Builder.of(SummonedVexEntity::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8), SummonedVexEntity::createAttributes);
    public static final DeferredHolder<EntityType<?>, EntityType<SummonedWitherSkeletonEntity>> SUMMONED_WITHER_SKELETON_ENTITY = registerEntity("summoned_wither_skeleton", () -> EntityType.Builder.of(SummonedWitherSkeletonEntity::new, MobCategory.MONSTER).sized(0.6F, 2.5F).clientTrackingRange(8), SummonedWitherSkeletonEntity::createAttributes);
    public static final DeferredHolder<EntityType<?>, EntityType<SmartBoltEntity>> SMART_BOLT_ENTITY = registerEntity("smart_bolt", () -> EntityType.Builder.of(SmartBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(HLSpells.MODID + ":" + name));
    }

    private static <T extends LivingEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier, Supplier<AttributeSupplier.Builder> attributeSupplier) {
        DeferredHolder<EntityType<?>, EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier.get(), p.factory.get().build()));
    }

    private record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
