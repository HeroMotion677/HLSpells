package com.divinity.hlspells.entities;

import net.minecraft.world.entity.player.Player;

public interface Summonable {

    Player getSummonedOwner();

    void setSummonedOwner(Player owner);
}
