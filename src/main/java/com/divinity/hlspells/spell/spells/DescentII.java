package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventDispatcher;

public class DescentII extends Spell {

    public DescentII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, true, maxSpellLevel);
    }
    int flag = 0;
@Override
    protected SpellConsumer<Player> getAction() {
        return p -> {

            if (!p.verticalCollisionBelow && flag == 0) {
                p.setForcedPose(Pose.FALL_FLYING);
                float yaw = p.getYRot();
                float pitch = p.getXRot();
                float f = 0.3F;
                double motionX = (double)(-Math.sin(yaw / 180.0F * (float)Math.PI) * Math.cos(pitch / 180.0F * (float)Math.PI) * f);
                double motionZ = (double)(Math.cos(yaw / 180.0F * (float)Math.PI) * Math.cos(pitch / 180.0F * (float)Math.PI) * f);
                p.setDeltaMovement(motionX, -0.1, motionZ);

            }
            return true;
        };
    }
    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.DESCENT.get();
    }
}
