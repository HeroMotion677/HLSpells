package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.VexEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HLSpells.MODID);

    public static final RegistryObject<EntityType<InvisibleTargetingEntity>> INVISIBLE_TARGETING_ENTITY = ENTITIES.register("invisible_targeting_entity", () ->
            EntityType.Builder.of(InvisibleTargetingEntity::new, EntityClassification.MISC).build(HLSpells.MODID + "invisible_targeting_entity"));

    public static final RegistryObject<EntityType<PiercingBoltEntity>> PIERCING_BOLT_ENTITY = ENTITIES.register("piercing_bolt", () ->
            EntityType.Builder.of(PiercingBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "piercing_bolt"));

    public static final RegistryObject<EntityType<FlamingBoltEntity>> FLAMING_BOLT_ENTITY = ENTITIES.register("flaming_bolt", () ->
            EntityType.Builder.of(FlamingBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "flaming_bolt"));

    public static final RegistryObject<EntityType<AquaBoltEntity>> AQUA_BOLT_ENTITY = ENTITIES.register("aqua_bolt", () ->
            EntityType.Builder.of(AquaBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (HLSpells.MODID + "aqua_bolt"));

    public static final RegistryObject<EntityType<SummonedVexEntity>> SUMMONED_VEX_ENTITY = ENTITIES.register("summoned_vex", () ->
            EntityType.Builder.of(SummonedVexEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.4F, 0.8F)
                    .clientTrackingRange(8).build(HLSpells.MODID + "summoned_vex"));

    @SubscribeEvent
    public static void attributeCreation(EntityAttributeCreationEvent event) {
        event.put(SUMMONED_VEX_ENTITY.get(), VexEntity.createAttributes().build());
    }
}
