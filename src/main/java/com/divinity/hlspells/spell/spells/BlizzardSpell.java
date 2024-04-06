package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class BlizzardSpell extends Spell {

    public BlizzardSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), p.level) {
                @Override
                public void tick() {
                    super.tick();
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(this.getX() - 5D), Mth.floor(this.getY() - 5D), Mth.floor(this.getZ() - 5D), Mth.floor(this.getX() + 5D), Mth.floor(this.getY() + 5D), Mth.floor(this.getZ() + 5D))) {
                        
                    }
                }
            };
            Util.shootSpellRelative(p, stormBullet, new Vec3(p.getX(), p.getY(), p.getZ()), 1.2F, 1.2F, 1.2F, true);
            return true;
        };
    }
}
