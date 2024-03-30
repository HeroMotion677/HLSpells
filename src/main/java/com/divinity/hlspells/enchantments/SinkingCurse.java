package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.setup.init.EnchantmentInit;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.divinity.hlspells.HLSpells.MODID;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

@Mod.EventBusSubscriber(modid = MODID)
public class SinkingCurse extends Enchantment {

    public SinkingCurse(EquipmentSlot... slots) {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, slots);
    }

    @SubscribeEvent
    public static void onArmorTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() != null) {
            LivingEntity entity = event.getEntity();
            for (ItemStack stack : entity.getArmorSlots())  {
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.CURSE_OF_SINKING.get(), stack) > 0) {
                    // FluidOnEyes check whether the player is currently in a liquid. If no liquids are found, the value is empty
                    if (entity.isInWater() && entity.getDeltaMovement().y() > -0.2D) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.2D, 0));
                    }
                }
            }
        }
    }

    @Override
    public boolean isCurse() {
        return true;
    }
}
