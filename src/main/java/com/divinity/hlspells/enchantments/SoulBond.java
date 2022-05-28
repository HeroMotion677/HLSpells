package com.divinity.hlspells.enchantments;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class SoulBond extends Enchantment {

    public SoulBond(EquipmentSlot... slots) {
        super(Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, slots);
    }

    @SubscribeEvent
    public static void onEntityHit(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof Player player && event.getEntity() != null && !player.level.isClientSide()) {
            for (InteractionHand hand : InteractionHand.values()) {
                if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_SYPHON.get(), player.getItemInHand(hand)) > 0) {
                    if (player.getRandom().nextInt(4) == 1) player.heal(0.5F);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isCurse() {
        return false;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchantment) {
        return enchantment != Enchantments.VANISHING_CURSE && enchantment != Enchantments.BINDING_CURSE;
    }
}