package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class EffectSpell<T extends MobEffect> extends Spell {

    private final T effect;
    private int duration;
    private int amplifier;
    private boolean isVisible;
    private final MobEffectInstance instance;

    public EffectSpell(T effect, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
        this.effect = effect;
        this.duration = Integer.MAX_VALUE;
        this.amplifier = 1;
        this.isVisible = false;
        this.instance = new MobEffectInstance(this.effect, this.duration, this.amplifier, false, false, false);
    }

    public EffectSpell(T effect, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        this(effect, type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
        this.tickDelay = tickDelay;
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            p.addEffect(instance);
            MobEffectInstance instance = p.getEffect(this.effect);
            if (instance != null) {
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(instance.getEffect());
                        cap.setEffectDuration(instance.getDuration());
                        cap.setEffectAmplifier(instance.getAmplifier());
                    }
                });
            }
            if (p.level.getBlockState(p.blockPosition().below()).getBlock() == Blocks.AIR) {
                for (int i = 0; i < 3; i++) {
                    p.level.addParticle(ParticleTypes.CLOUD, p.getX(), p.getY() - 1, p.getZ(), 0, p.getDeltaMovement().y, 0);
                }
            }
            return true;
        };
    }

    public EffectSpell<T> duration(int duration) {
        this.duration = duration;
        return this;
    }

    public EffectSpell<T> amplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public EffectSpell<T> visible(boolean isVisible) {
        this.isVisible = isVisible;
        return this;
    }
}
