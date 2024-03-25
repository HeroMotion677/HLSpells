package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class LightningII extends Spell {

    public LightningII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, true, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), p.level) {
                @Override
                public void tick() {
                    super.tick();
                    if (this.getInitialPosition() != null) {
                        float distance = Mth.sqrt((float) distanceToSqr(this.getInitialPosition()));
                        if (distance >= 25) {
                            this.remove(RemovalReason.DISCARDED);
                        }
                        if (this.level.getGameTime() % 2 == 0) {
                            LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
                            lightning.moveTo(this.getX(), this.getY(), this.getZ());
                            this.level.addFreshEntity(lightning);
                        }
                    }
                }
            };
            stormBullet.setOwner(p);
            stormBullet.setInitialPosition(p.position());
            stormBullet.setPos(p.getX() + p.getViewVector(1.0F).x, p.getY() + 1.35, p.getZ() + p.getViewVector(1.0F).z);
            stormBullet.shootFromRotation(p, p.xRot, p.yRot, 1.3F, 1.3F, 1.3F);
            p.level.addFreshEntity(stormBullet);
            return true;
        };
    }

    @Nullable
    @Override
    public Spell getUpgrade() {
        return SpellInit.LIGHTNING_III.get();
    }

    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.LIGHTNING.get();
    }
}
