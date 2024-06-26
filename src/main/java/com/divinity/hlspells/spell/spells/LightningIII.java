package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import com.divinity.hlspells.util.Util;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class LightningIII extends Spell {

    public LightningIII(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, true, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            Util.getEntitiesInRange(p, LivingEntity.class, 40, 40, 40, f ->  {
                List<? extends String> blacklistedMobs = HLSpells.CONFIG.lightningSpellList.get();
                boolean predicate = false;
                for (String id : blacklistedMobs) {
                    if (id.equals(ForgeRegistries.ENTITY_TYPES.getKey(f.getType()) != null ? ForgeRegistries.ENTITY_TYPES.getKey(f.getType()).toString() : "")) {
                        predicate = true;
                    }
                }
                return !(f instanceof Summonable) && !(f.isAlliedTo(p)) && !predicate && f != p && !(f instanceof ArmorStand);
            }).forEach(t -> {
                LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, p.level);
                lightning.moveTo(t.getX(), t.getOnPos().getY(), t.getZ());
                p.level.addFreshEntity(lightning);
            });
            return true;
        };
    }

    @Override
    public Spell getUpgradeableSpellPath() {
        return SpellInit.LIGHTNING.get();
    }
}
