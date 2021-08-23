package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.init.EnchantmentInit;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static com.divinity.hlspells.HLSpells.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class SinkingCurse extends Enchantment
{
    public SinkingCurse(EquipmentSlotType... slots) {
        super(Rarity.UNCOMMON, EnchantmentType.ARMOR, slots);
    }

    @SubscribeEvent
    public static void onArmorTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() != null) {
            LivingEntity entity = event.getEntityLiving();
            for (ItemStack stack : entity.getArmorSlots())  {
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.CURSE_OF_SINKING.get(), stack) > 0) {
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
