package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class SlowFallSpell extends Spell {

    private static final MobEffectInstance SLOW_FALLING = new MobEffectInstance(MobEffects.SLOW_FALLING, Integer.MAX_VALUE, 5, false, false);

    public SlowFallSpell(String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        super(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, displayName, xpCost, tickDelay, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            if (p.getDeltaMovement().y <= 0) {
                MobEffectInstance effect = p.getEffect(MobEffects.SLOW_FALLING);
                if (effect != null && effect.isVisible()) {
                    p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                        if (cap.getEffect() == null) {
                            cap.setEffect(effect.getEffect());
                            cap.setEffectDuration(effect.getDuration());
                            cap.setEffectAmplifier(effect.getAmplifier());
                        }
                    });
                }
                p.addEffect(SLOW_FALLING);
                if (p.level.getBlockState(p.blockPosition().below()).getBlock() == Blocks.AIR) {
                    for (int i = 0; i < 3; i++) {
                        p.level.addParticle(ParticleTypes.CLOUD, p.getX(), p.getY() - 1, p.getZ(), 0, p.getDeltaMovement().y, 0);
                    }
                }
                return true;
            }
            return false;
        };
    }
}
