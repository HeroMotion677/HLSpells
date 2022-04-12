package com.divinity.hlspells.items.armor;

import com.divinity.hlspells.models.WizardHatModel;
import com.divinity.hlspells.renderers.RenderRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.function.Consumer;

import static com.divinity.hlspells.HLSpells.MODID;



public class WizardHatArmorItem extends ArmorItem implements IItemRenderProperties {
    public WizardHatArmorItem(ArmorMaterial material, EquipmentSlot type, Properties properties) {
        super(material, type, properties);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.HEAD ? MODID  + ":" + "textures/models/armor/wizard_hat.png" : null;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Nullable
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                HumanoidModel<LivingEntity> armorModel = RenderRegistry.armorModel.get(itemStack.getItem());
                if (armorModel != null) {
                    armorModel.head.visible = armorSlot == EquipmentSlot.HEAD;
                }
                System.out.println(armorModel == null);
                return armorModel;
            }
        });
    }
}
