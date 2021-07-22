package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.StormBulletEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HLSpells.MODID);

    public static final RegistryObject<EntityType<StormBulletEntity>> STORM_BULLET_ENTITY = ENTITIES.register("stormbulletentity", () ->
            EntityType.Builder.of(StormBulletEntity::new, EntityClassification.MISC).build(new ResourceLocation(HLSpells.MODID, "textures").toString()));
}
