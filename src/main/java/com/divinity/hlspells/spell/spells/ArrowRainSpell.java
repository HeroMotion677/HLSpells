package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ArrowRainSpell extends Spell {

    public ArrowRainSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            PlayerCapProvider.get(p).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                if (p.level().isClientSide()) {
                    if (cap.getSpellTimer() % 6 == 0) {
                        doCloudParticles(p, p.level());
                    }
                }
                else {
                    if (cap.getSpellTimer() % 6 == 0) {
                        for (int i = 0; i < 10; i++) {
                            doArrowSpawn(p, p.level());
                        }
                        cap.setSpellTimer(0);
                    }
                }
            });
            return true;
        };
    }

    private static void doArrowSpawn(Player player, Level world) {
        Arrow arrowEntity = new Arrow(world,
                player.getX() + (world.random.nextDouble() - 0.5D) * player.getBbWidth(),
                player.getY() + 3.2, player.getZ() + (world.random.nextDouble() - 0.5D) * player.getBbWidth(),
                new ItemStack(Items.ARROW), null);

        arrowEntity.shootFromRotation(player, player.xRot, player.yRot, 1.0F, 1.3F, 1.0F);
        arrowEntity.setDeltaMovement(Mth.cos((float) Math.toRadians(player.yRot + 90)) + (world.random.nextFloat() - 0.5F) * player.getBbWidth(), -0.6, Mth.sin((float) Math.toRadians(player.yRot + 90)) + (world.random.nextFloat() - 0.5F) * player.getBbWidth());
        world.addFreshEntity(arrowEntity);
    }

    private static void doCloudParticles(Player player, Level world) {
        for (int i = 0; i < 5; i++) {
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 4, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 4, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 4, player.getZ(), 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 5, player.getZ() + 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX(), player.getY() + 5, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 4, player.getZ() + 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 4, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() + 0.45, player.getY() + 4, player.getZ() - 0.45, 0, 0, 0);
            world.addParticle(ParticleTypes.CLOUD, player.getX() - 0.45, player.getY() + 4, player.getZ() + 0.45, 0, 0, 0);
        }
    }
}
