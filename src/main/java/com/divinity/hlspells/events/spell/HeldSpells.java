package com.divinity.hlspells.events.spell;

import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.HLSpells;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.client.renderer.entity.model.ShulkerBulletModel;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.passive.horse.ZombieHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class HeldSpells
{
    private int timer = 0;
    private int otherTimer = 0;
    private int healingTimer = 0;
    private int airTimer = 0;
    private boolean flag =  true;


    @SubscribeEvent
    public void activeSpells (TickEvent.PlayerTickEvent event)
    {
        if (SpellBookItem.isHeldActive && event.player != null)
        {
            if (event.player.getMainHandItem().getItem() instanceof SpellBookItem || event.player.getOffhandItem().getItem() instanceof SpellBookItem)
            {
                if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.ARROW_RAIN.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.ARROW_RAIN.get())
                {
                    doArrowRain(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.FEATHER_FALLING.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.FEATHER_FALLING.get())
                {
                    doFeatherFalling(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.HEALING_CIRCLE.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.HEALING_CIRCLE.get())
                {
                    doHealingCircle(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.LEVITATION.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.LEVITATION.get())
                {
                    doLevitation(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.PROTECTION_CIRCLE.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.PROTECTION_CIRCLE.get())
                {
                    doProtectionCircle(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.RESPIRATION.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.RESPIRATION.get())
                {
                    doRespiration(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.SPEED.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.SPEED.get())
                {
                    doSpeed(event.player);
                }

                else if (SpellUtils.getSpellBook(event.player.getMainHandItem()) == SpellBookInit.ABSORBING.get() || SpellUtils.getSpellBook(event.player.getOffhandItem()) == SpellBookInit.ABSORBING.get())
                {
                    doAbsorbing(event.player);
                }
            }
        }

        else if (!SpellBookItem.isHeldActive && event.player != null)
        {
            resetEffects(event.player);
        }
    }

    // For removing multiple enchants when the player hovers over the spell book after the enchanting is finished
    @SubscribeEvent
    public void tooltipEvent (ItemTooltipEvent event)
    {
        if (event.getPlayer() != null && event.getToolTip() != null)
        {
            if (event.getItemStack().getItem() instanceof SpellBookItem)
            {
                ItemStack stack = event.getItemStack();
                if (stack.getEnchantmentTags().size() > 1)
                {
                    for (int i = 0; i < stack.getEnchantmentTags().size() && stack.getEnchantmentTags().size() > 1; i++)
                    {
                        stack.getEnchantmentTags().remove(i);
                    }
                }
            }
        }
    }

    // Feather Falling
    private void doFeatherFalling (PlayerEntity player)
    {
        if (player.getDeltaMovement().y <= 0)
        {
            player.addEffect(new EffectInstance(Effects.SLOW_FALLING, Integer.MAX_VALUE, 2, false, false));
            for (int a = 0; a < 9; a++)
            {
                player.getCommandSenderWorld().addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() - 1,
                        player.getZ(), 0, player.getDeltaMovement().y, 0);
            }
        }
    }

    // Protection Circle
    private void doProtectionCircle(PlayerEntity player)
    {
        List<LivingEntity> livingEntities = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        for (LivingEntity entity : livingEntities)
        {
            if (!(entity instanceof PlayerEntity)) entity.setDeltaMovement(entity.getLookAngle().reverse().multiply(0.3D,0,0.3D));
        }
    }

    // Levitation
    private void doLevitation (PlayerEntity player)
    {
        if (player.getDeltaMovement().y >= 0)
        {
            player.addEffect(new EffectInstance(Effects.LEVITATION, Integer.MAX_VALUE, 2, false, false));

            for (int a = 0; a < 1; a++)
            {
                player.getCommandSenderWorld().addParticle(ParticleTypes.END_ROD, player.getX(), player.getY() - 1,
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
                player.getY() + 4, player.getZ() + (player.getCommandSenderWorld().random.nextDouble() - 0.5D) * (double) player.getBbWidth());

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

    // Healing Circle
    private void doHealingCircle (PlayerEntity player)
    {
        List<LivingEntity> livingEntities = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(player.getX() - 6, player.getY() - 6, player.getZ() - 6,
                        player.getX() + 6, player.getY() + 6, player.getZ() + 6), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        World world = player.getCommandSenderWorld();

        healingTimer++;

        if (healingTimer % 10 == 0)
        {
            doHealingParticles(player, world);
        }

        if (healingTimer % 20 == 0)
        {
            for (LivingEntity entities : livingEntities)
            {
                doRadiusParticles(entities);

                if (entities instanceof PhantomEntity || entities instanceof SkeletonEntity || entities instanceof SkeletonHorseEntity
                        || entities instanceof WitherEntity || entities instanceof WitherSkeletonEntity || entities instanceof ZoglinEntity
                        || entities instanceof ZombieEntity || entities instanceof ZombieHorseEntity)
                {
                    entities.setLastHurtByPlayer(player);
                    entities.hurt(DamageSource.MAGIC, 1.0F);
                }

                else
                {
                    entities.heal(1.0F);
                }
            }
            healingTimer = 0;
        }
    }

    private void doHealingParticles(PlayerEntity player, World world)
    {
        for (int i = 0; i < 2; i++)
        {
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 0 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 0 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 3 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 3 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 4 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 4 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 5 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 5 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 6 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 6 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() + 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() - 2 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 1 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 1 + world.random.nextFloat(), 0, 0, 0);
            player.getCommandSenderWorld().addParticle(ParticleTypes.ENCHANT, player.getX() - 2 + world.random.nextFloat(), player.getY() + 0.2, player.getZ() + 2 + world.random.nextFloat(), 0, 0, 0);
        }

        // Ring
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 1, player.getY() + 1.2, player.getZ() - 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() + 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX(), player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 1, player.getY() + 1.2, player.getZ() + 6, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 2, player.getY() + 1.2, player.getZ() + 5, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 3, player.getY() + 1.2, player.getZ() + 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 4, player.getY() + 1.2, player.getZ() + 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 5, player.getY() + 1.2, player.getZ() + 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ() + 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ(), 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 6, player.getY() + 1.2, player.getZ() - 1, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 5, player.getY() + 1.2, player.getZ() - 2, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 4, player.getY() + 1.2, player.getZ() - 3, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 3, player.getY() + 1.2, player.getZ() - 4, 0, 0, 0);
        player.getCommandSenderWorld().addParticle(ParticleTypes.HAPPY_VILLAGER, player.getX() - 2, player.getY() + 1.2, player.getZ() - 5, 0, 0, 0);
    }

    private void doRadiusParticles(Entity entities)
    {
        if (Minecraft.getInstance().player != null)
        {
            World clientWorld = Minecraft.getInstance().player.getCommandSenderWorld();

            if (entities instanceof PhantomEntity || entities instanceof SkeletonEntity || entities instanceof SkeletonHorseEntity
               || entities instanceof WitherEntity || entities instanceof WitherSkeletonEntity || entities instanceof ZoglinEntity
               || entities instanceof ZombieEntity || entities instanceof ZombieHorseEntity)
            {
                for (int i = 0; i < 5; i++)
                {
                    double d0 = (entities.getX() + clientWorld.random.nextFloat());
                    double d1 = (entities.getY() + clientWorld.random.nextFloat());
                    double d2 = (entities.getZ() + clientWorld.random.nextFloat());
                    double d3 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    double d4 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    double d5 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                    clientWorld.addParticle(ParticleTypes.SMOKE, d0, d1, d2, d3, d4, d5);
                }
            }

            else
            {
                double d0 = (entities.getX() + (clientWorld.random.nextFloat() - 0.5D));
                double d1 = (entities.getY() + (clientWorld.random.nextFloat() - 0.3D));
                double d2 = (entities.getZ() + (clientWorld.random.nextFloat() - 0.5D));
                double d3 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                double d4 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                double d5 = (clientWorld.random.nextFloat() - 0.2D) * 0.5D;
                clientWorld.addParticle(ParticleTypes.HEART, d0, d1, d2, d3,d4, d5);
            }
        }
    }

    // Speed
    private void doSpeed (PlayerEntity player)
    {
        ModifiableAttributeInstance attribute = player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);

        if (attribute != null) {
            attribute.setBaseValue(0.10000000149011612F);
        }
    }

    // Respiration
    private void doRespiration (PlayerEntity player)
    {
        List<PlayerEntity> players = player.getCommandSenderWorld().getEntitiesOfClass(PlayerEntity.class,
                new AxisAlignedBB(player.getX() - 10, player.getY() - 4, player.getZ() - 10,
                        player.getX() + 10, player.getY() + 4, player.getZ() + 10), null)
                .stream().sorted(new Object() {Comparator<Entity> compareDistOf(double x, double y, double z) {return Comparator.comparing(axis -> axis.distanceToSqr(x, y, z));}}
                        .compareDistOf(player.getX(), player.getY(), player.getZ())).collect(Collectors.toList());

        airTimer++;
        for (PlayerEntity p : players)
        {
            if (p.isUnderWater() && airTimer == 10)
            {
                p.setAirSupply(p.getAirSupply() + 15);
                if (p.getAirSupply() > p.getMaxAirSupply())
                {
                    p.setAirSupply(p.getMaxAirSupply());
                }
                airTimer = 0;
            }
        }
    }


    // Absorbing
    private void doAbsorbing (PlayerEntity playerEntity)
    {}


    private void resetEffects (PlayerEntity playerEntity)
    {
        timer = 0;
        otherTimer = 0;
        healingTimer = 0;
        airTimer = 0;
        ModifiableAttributeInstance instance = playerEntity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
        if (instance != null)
        {
            instance.setBaseValue(0.10000000149011612F);
        }

        playerEntity.removeEffect(Effects.SLOW_FALLING);
        playerEntity.removeEffect(Effects.LEVITATION);
    }
}
