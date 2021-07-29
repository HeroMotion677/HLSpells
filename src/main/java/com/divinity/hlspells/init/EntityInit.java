package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.AquaBoltEntity;
import com.divinity.hlspells.entities.FlamingBoltEntity;
import com.divinity.hlspells.entities.PiercingBoltEntity;
import com.divinity.hlspells.entities.InvisibleBoltEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HLSpells.MODID);

    public static final RegistryObject<EntityType<InvisibleBoltEntity>> STORM_BULLET_ENTITY = ENTITIES.register("stormbulletentity", () ->
            EntityType.Builder.of(InvisibleBoltEntity::new, EntityClassification.MISC).build(new ResourceLocation(HLSpells.MODID, "textures").toString()));

    public static final RegistryObject<EntityType<PiercingBoltEntity>> PIERCING_BOLT_ENTITY = ENTITIES.register("piercingboltentity", () ->
            EntityType.Builder.of(PiercingBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (new ResourceLocation(HLSpells.MODID, "textures/entity/green_bolt.png").toString()));

    public static final RegistryObject<EntityType<FlamingBoltEntity>> FLAMING_BOLT_ENTITY = ENTITIES.register("flamingboltentity", () ->
            EntityType.Builder.of(FlamingBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (new ResourceLocation(HLSpells.MODID, "textures/entity/orange_bolt.png").toString()));

    public static final RegistryObject<EntityType<AquaBoltEntity>> AQUA_BOLT_ENTITY = ENTITIES.register("aquaboltentity", () ->
            EntityType.Builder.of(AquaBoltEntity::new, EntityClassification.MISC).sized(0.3125F, 0.3125F).build
                    (new ResourceLocation(HLSpells.MODID, "textures/entity/blue_bolt.png").toString()));
}
