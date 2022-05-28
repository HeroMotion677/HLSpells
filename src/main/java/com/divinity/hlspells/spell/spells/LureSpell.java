package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.goal.SpellBookLureGoal;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.divinity.hlspells.entities.goal.SpellBookLureGoal.LURE_RANGE;

public class LureSpell extends Spell {

    private static final MobEffectInstance GLOWING = new MobEffectInstance(MobEffects.GLOWING, Integer.MAX_VALUE, 5, false, false);

    public LureSpell(String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        super(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, displayName, xpCost, tickDelay, treasureOnly);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            MobEffectInstance effect = p.getEffect(MobEffects.GLOWING);
            if (effect != null && effect.isVisible()) {
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(effect.getEffect());
                        cap.setEffectDuration(effect.getDuration());
                        cap.setEffectAmplifier(effect.getAmplifier());
                    }
                });
            }
            p.addEffect(GLOWING);
            List<Mob> mobEntities = Util.getEntitiesInRange(p, Mob.class, LURE_RANGE, LURE_RANGE, LURE_RANGE);
            for (Mob mob : mobEntities) {
                List<? extends String> blacklistedMobs = HLSpells.CONFIG.sapientMobsList.get();
                boolean predicate = false;
                for (String id : blacklistedMobs) {
                    if (id.equals(mob.getType().getRegistryName() != null ? mob.getType().getRegistryName().toString() : "")) {
                        predicate = true;
                    }
                }
                if (!predicate && mob.goalSelector.getRunningGoals().noneMatch(pr -> pr.getGoal() instanceof SpellBookLureGoal)) {
                    mob.goalSelector.addGoal(0, new SpellBookLureGoal(mob, 1.0D));
                }
            }
            return true;
        };
    }
}
