package com.divinity.hlspells.events;

import com.divinity.hlspells.HLSpells;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
public class ModelLayers {

    public static final ModelLayerLocation WIZARD_HAT_LAYER = new ModelLayerLocation(new ResourceLocation(HLSpells.MODID, "wizard_hat"), "hat_main");

    public static final ModelLayerLocation FIRE_BALL_LAYER = new ModelLayerLocation(new ResourceLocation(HLSpells.MODID, "fireballmodel"), "main");

    public static final ModelLayerLocation BOLT_LAYER = new ModelLayerLocation(new ResourceLocation(HLSpells.MODID,"baseboltmodel"), "main");

    public static ModelLayerLocation register(String name) {
        return register(name, "main");
    }

    public static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(new ResourceLocation(HLSpells.MODID, name), layer);
    }

    }

