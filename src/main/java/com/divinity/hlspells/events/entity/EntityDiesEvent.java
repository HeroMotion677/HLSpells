package com.divinity.hlspells.events.entity;

import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.items.ModTotemItem;
import com.divinity.hlspells.HLSpells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class EntityDiesEvent
{
    @SubscribeEvent
    public static void onEntityDies(LivingDeathEvent event) {
        LivingEntity player = event.getEntityLiving();
        World world = player.level;
        if (event.getEntityLiving() instanceof PlayerEntity) {
            for (Hand hand : Hand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);
                // TOTEM OF GRIEFING
                if (heldItem.getItem() == ItemInit.TOTEM_OF_GRIEFING.get()) {
                    ModTotemItem.vanillaTotemBehavior(event, player, heldItem, ItemInit.TOTEM_OF_GRIEFING.get());
                    world.explode(player, player.getX(), player.getY(), player.getZ(), 10.0F, Explosion.Mode.BREAK);
                }
                // TOTEM OF ESCAPING
                if (heldItem.getItem() == ItemInit.TOTEM_OF_ESCAPING.get()) {
                    ModTotemItem.vanillaTotemBehavior(event, player, heldItem, ItemInit.TOTEM_OF_ESCAPING.get());
                    player.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                    // teleport the player randomly nearby
                    for(int i = 0; i < 16; ++i) {
                        double xRand = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = MathHelper.clamp(player.getY() + (double) (player.getRandom().nextInt(16) - 8), 0.0D, (double) (world.getHeight() - 1));
                        double zRand = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                        player.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                }
                // TOTEM OF RETURNING
                if (heldItem.getItem() == ItemInit.TOTEM_OF_RETURNING.get()) {
                    ModTotemItem.vanillaTotemBehavior(event, player, heldItem, ItemInit.TOTEM_OF_RETURNING.get());
                    // not functional yet, still cleaning up
                }
            }
        }
    }
}