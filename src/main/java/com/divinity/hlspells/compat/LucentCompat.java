package com.divinity.hlspells.compat;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.spells.IlluminateSpell;
import com.divinity.hlspells.util.SpellUtils;
import com.legacy.lucent.api.plugin.ILucentPlugin;
import com.legacy.lucent.api.plugin.LucentPlugin;
import com.legacy.lucent.api.registry.EntityLightingRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@LucentPlugin
@OnlyIn(Dist.CLIENT)
public final class LucentCompat implements ILucentPlugin {

    @Override
    public String ownerModID() {
        return HLSpells.MODID;
    }

    @Override
    public void registerEntityLightings(EntityLightingRegistry registry) {
        registry.register(EntityType.PLAYER, (Player player) -> {
            if (player instanceof AbstractClientPlayer clientPlayer) {
                if (SpellUtils.getSpell(clientPlayer.getUseItem()) instanceof IlluminateSpell spell && spell.canUseSpell()) {
                    return 15;
                }
            }
            return 0;
        });
    }
}
