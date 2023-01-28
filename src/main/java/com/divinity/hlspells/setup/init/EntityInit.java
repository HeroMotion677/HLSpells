package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.living.summoned.SummonedSkeletonEntity;
import com.divinity.hlspells.entities.living.summoned.SummonedVexEntity;
import com.divinity.hlspells.entities.projectile.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = HLSpells.MODID)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HLSpells.MODID);
    private static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<InvisibleTargetingEntity>> INVISIBLE_TARGETING_ENTITY = registerEntity("invisible_targeting_entity", () -> EntityType.Builder.of(InvisibleTargetingEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<KnockbackBoltEntity>> KNOCKBACK_BOLT_ENTITY = registerEntity("knockback_bolt", () -> EntityType.Builder.of(KnockbackBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<PiercingBoltEntity>> PIERCING_BOLT_ENTITY = registerEntity("piercing_bolt", () -> EntityType.Builder.<PiercingBoltEntity>of(PiercingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<FlamingBoltEntity>> FLAMING_BOLT_ENTITY = registerEntity("flaming_bolt", () -> EntityType.Builder.of(FlamingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<AquaBoltEntity>> AQUA_BOLT_ENTITY = registerEntity("aqua_bolt", () -> EntityType.Builder.of(AquaBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<FreezingBoltEntity>> FREEZING_BOLT_ENTITY = registerEntity("freezing_bolt", () -> EntityType.Builder.of(FreezingBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<ChorusBoltEntity>> CHORUS_BOLT_ENTITY = registerEntity("chorus_bolt", () -> EntityType.Builder.of(ChorusBoltEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F));
    public static final RegistryObject<EntityType<WitherSkullEntity>> WITHER_SKULL_ENTITY = registerEntity("wither_skull", () -> EntityType.Builder.of(WitherSkullEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<SummonedVexEntity>> SUMMONED_VEX_ENTITY = registerEntity("summoned_vex", () -> EntityType.Builder.of(SummonedVexEntity::new, MobCategory.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8), SummonedVexEntity::createAttributes);
    public static final RegistryObject<EntityType<SummonedSkeletonEntity>> SUMMONED_SKELETON_ENTITY = registerEntity("summoned_skeleton", () -> EntityType.Builder.of(SummonedSkeletonEntity::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8), Skeleton::createAttributes);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(HLSpells.MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier, Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier.get(), p.factory.get().build()));
    }

    private record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
