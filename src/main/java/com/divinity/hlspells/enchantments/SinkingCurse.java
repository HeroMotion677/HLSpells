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

@Mod.EventBusSubscriber(modid = MODID)
public class SinkingCurse extends Enchantment {

    public SinkingCurse(EquipmentSlot... slots) {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, slots);
    }

    @SubscribeEvent
    public static void onArmorTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() != null) {
            LivingEntity entity = event.getEntityLiving();
            for (ItemStack stack : entity.getArmorSlots())  {
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.CURSE_OF_SINKING.get(), stack) > 0) {
                    // FluidOnEyes check whether the player is currently in a liquid. If no liquids are found, the value is null
                    // noinspection ConstantConditions
                    if (entity.fluidOnEyes != null && entity.getDeltaMovement().y() > -0.2D) {
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
