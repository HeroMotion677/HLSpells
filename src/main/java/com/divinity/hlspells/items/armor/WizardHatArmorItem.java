package com.divinity.hlspells.items.armor;

import com.divinity.hlspells.models.WizardHatModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

import static com.divinity.hlspells.HLSpells.MODID;

import net.minecraft.world.item.Item.Properties;

public class WizardHatArmorItem extends ArmorItem {
    public WizardHatArmorItem(ArmorMaterial material, EquipmentSlot type, Properties properties) {
        super(material, type, properties);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return MODID  + ":" + "textures/armor/wizard_hat.png";
    }

}
