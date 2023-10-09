package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventDispatcher;

public class DescentII extends Spell {

    public DescentII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        super(type, rarity, tier, marker, displayName, xpCost, tickDelay, true, maxSpellLevel);
    }
@Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            var players = Util.getEntitiesInRange(p, Player.class, 3, 3, 3);
            p.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(cap -> {
                cap.setSpellTimer(cap.getSpellTimer() + 1);
                for (Player player : players) {
                    if(!player.isFallFlying() && !player.isOnGround()) {
                        player.startFallFlying();
                    }
                }
            });


            return true;
        };
    }
    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.DESCENT.get();
    }
}
