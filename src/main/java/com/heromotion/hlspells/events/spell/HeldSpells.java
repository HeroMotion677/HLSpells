package com.heromotion.hlspells.events.spell;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.items.SpellBookItem;
import com.heromotion.hlspells.util.SpellUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class HeldSpells
{
    private int timer = 0;
    private int otherTimer = 0;
    private boolean flag =  true;

    @SubscribeEvent
    public void activeSpells (TickEvent.PlayerTickEvent event)
    {
        if (SpellBookItem.isHeldActive && event.player != null && event.player.getMainHandItem().getItem() instanceof SpellBookItem)
        {
            if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.ARROW_RAIN.get())
            {
                doArrowRain(event.player);
            }

            else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.FEATHER_FALLING.get())
            {
                doFeatherFalling(event.player);
            }
        }

        else if (!SpellBookItem.isHeldActive && event.player != null)
        {
            timer = 0;
            otherTimer = 0;
            event.player.removeEffect(Effects.SLOW_FALLING);
        }
    }

    // Feather Falling
    private void doFeatherFalling (PlayerEntity player)
    {
        player.addEffect(new EffectInstance(Effects.SLOW_FALLING, Integer.MAX_VALUE, 2, false, false));

        if (player.getDeltaMovement().y < 0)
        {
            for (int a = 0; a < 9; a++)
            {
                player.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
        }
    }

    // Arrow Rain
    private void doArrowRain(PlayerEntity player)
    {
        if (player.getCommandSenderWorld().isClientSide())
        {
            if (flag)
                doParticles(player);
            flag = false;

            otherTimer++;
            if (otherTimer % 15 == 0)
            {
                flag = true;
                otherTimer = 0;
            }
        }

        else if (!player.getCommandSenderWorld().isClientSide())
        {
            timer++;
            if (timer % 15 == 0)
            {
                for (int i = 0; i < 5; i++)
                {
                    doArrowSpawn(player);
                }
                timer = 0;
            }
        }
    }

    private void doArrowSpawn(PlayerEntity player)
    {
        ArrowEntity arrowEntity = new ArrowEntity(player.getCommandSenderWorld(),
                player.getX() + (player.getCommandSenderWorld().random.nextDouble() - 0.5D) * (double) player.getBbWidth(),
                player.getY() + 5, player.getZ() + (player.getCommandSenderWorld().random.nextDouble() - 0.5D) * (double) player.getBbWidth());

        arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, 1.0F, 1.0F);
        arrowEntity.setDeltaMovement(MathHelper.cos((float) Math.toRadians(player.yRot + 90)) + (player.getCommandSenderWorld().random.nextFloat() - 0.5F) * player.getBbWidth(), -0.6, MathHelper.sin((float) Math.toRadians(player.yRot + 90)) + (player.getCommandSenderWorld().random.nextFloat() - 0.5F) * player.getBbWidth());
        player.getCommandSenderWorld().addFreshEntity(arrowEntity);
    }

    private void doParticles(PlayerEntity playerEntity)
    {
        for (int i = 0; i < 5; i++)
        {
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ(), 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX(), playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() + 0.45, playerEntity.getY() + 5, playerEntity.getZ() - 0.45, 0, 0, 0);
            playerEntity.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, playerEntity.getX() - 0.45, playerEntity.getY() + 5, playerEntity.getZ() + 0.45, 0, 0, 0);
        }
    }
}
