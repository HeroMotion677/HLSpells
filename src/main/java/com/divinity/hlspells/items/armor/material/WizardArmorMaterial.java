package com.divinity.hlspells.items.armor.material;

import com.divinity.hlspells.HLSpells;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class WizardArmorMaterial {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, HLSpells.MODID);

    public static final int DURABILITY_MULTIPLIER = 8;

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WIZHAT = ARMOR_MATERIALS.register("wizhat", () -> {
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.HELMET, 1);
        defense.put(ArmorItem.Type.CHESTPLATE, 0);
        defense.put(ArmorItem.Type.LEGGINGS, 0);
        defense.put(ArmorItem.Type.BOOTS, 0);
        defense.put(ArmorItem.Type.BODY, 0);
        return new ArmorMaterial(defense, 22, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(Items.LEATHER),
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "wizard_hat"))), 0.0F, 0.0F);
    });

    public static Holder<ArmorMaterial> wizardHat() {
        return WIZHAT;
    }
}
