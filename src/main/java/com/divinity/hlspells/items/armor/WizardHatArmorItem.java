package com.divinity.hlspells.items.armor;

import com.divinity.hlspells.events.ForgeClientEventHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import static com.divinity.hlspells.HLSpells.MODID;

public class WizardHatArmorItem extends ArmorItem {


    public WizardHatArmorItem(ArmorMaterial material, EquipmentSlot type, Properties properties) {
        super(material, type, properties);
    }

    @Nullable
    @Override

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return slot == EquipmentSlot.HEAD ? "%s:%s".formatted(MODID, "textures/items/armor/model/wizard_hat.png") : null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> properties) {
                var hatArmorModel = ForgeClientEventHandler.hatArmorModel.get(itemStack.getItem());
                if (hatArmorModel != null)
                    hatArmorModel.head.visible = (armorSlot == EquipmentSlot.HEAD);
                return hatArmorModel;
            }
        });
    }

}
