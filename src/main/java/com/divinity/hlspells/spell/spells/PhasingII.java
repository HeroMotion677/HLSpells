package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.StaffItem;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.util.Util;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

public class PhasingII extends Spell {

    public PhasingII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, true, maxSpellLevel);
    }

    private static final MobEffectInstance INVIS = new MobEffectInstance(MobEffects.INVISIBILITY, Integer.MAX_VALUE, 5, false, false, false);

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            this.canUse = !p.noPhysics && !p.onClimbable() && !p.isPassenger();
            if (canUse) {
                p.addEffect(INVIS);
                p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                    if (cap.getEffect() == null) {
                        cap.setEffect(INVIS.getEffect());
                        cap.setEffectDuration(0);
                        cap.setEffectAmplifier(INVIS.getAmplifier());
                        p.setInvulnerable(true);
                    }
                });
            }
            return this.canUse;
        };
    }
}
