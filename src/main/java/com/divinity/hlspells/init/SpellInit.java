package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(Spell.class, HLSpells.MODID);
    public static final RegistryObject<Spell> EMPTY = register("empty", () -> new Spell(SpellType.CAST, (p, w) -> false, "No Spell"));
    public static final RegistryObject<Spell> SLOW_FALL = register("slow_fall", () -> new Spell(SpellType.HELD, SpellActions::doSlowFall, "Slow Falling", 1, 15));
    public static final RegistryObject<Spell> BLAST = register("blast", () -> new Spell(SpellType.CAST, SpellActions::doBlastSpell, "Blast", 6));
    public static final RegistryObject<Spell> TELEPORT = register("teleport", () -> new Spell(SpellType.CAST, SpellActions::doTeleport, "Teleport", 9));
    public static final RegistryObject<Spell> ARROW_RAIN = register("arrow_rain", () -> new Spell(SpellType.HELD, SpellActions::doArrowRain, "Arrow Rain", 1, 6));
    public static final RegistryObject<Spell> BOLT = register("bolt", () -> new Spell(SpellType.CAST, SpellActions::doBoltSpell, "Bolt", 5));
    public static final RegistryObject<Spell> HEALING_CIRCLE = register("healing_circle", () -> new Spell(SpellType.HELD, SpellActions::doHealingCircle, "Healing Circle", 1, 3));
    public static final RegistryObject<Spell> SOUL_SUMMON = register("soul_summon", () -> new Spell(SpellType.CAST, SpellActions::doSummonSpell, "Soul Summon", 16));
    public static final RegistryObject<Spell> PULL = register("pull", () -> new Spell(SpellType.CAST, SpellActions::doPullSpell, "Pull", 3));
    public static final RegistryObject<Spell> BOND = register("bond", () -> new Spell(SpellType.CAST, SpellActions::doBondSpell, "Bond", 1));
    public static final RegistryObject<Spell> LIGHTNING_CHAIN = register("lightning_chain", () -> new Spell(SpellType.CAST, SpellActions::doLightingChain, "Lightning Chain", 14));
    public static final RegistryObject<Spell> FIRE_BALL = register("fire_ball", () -> new Spell(SpellType.CAST, SpellActions::doFireBallSpell, "Fire Ball", 5));
    public static final RegistryObject<Spell> LEVITATION = register("levitation", () -> new Spell(SpellType.HELD, SpellActions::doLevitation, "Levitation", 1, 6));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = register("lightning_bolt", () -> new Spell(SpellType.CAST, SpellActions::doLightningBolt, "Lightning Bolt", 8));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = register("knockback_bolt", () -> new Spell(SpellType.CAST, SpellActions::doKnockBackBolt, "Knockback Bolt", 3));
    //public static final RegistryObject<Spell> PROTECTION_CIRCLE = register("protection_circle", () -> new Spell(SpellType.HELD, SpellActions::doProtectionCircle, "Protection Circle", 1, 6));
    public static final RegistryObject<Spell> RESPIRATION = register("respiration", () -> new Spell(SpellType.HELD, SpellActions::doRespiration, "Respiration", 1, 15));
    public static final RegistryObject<Spell> SPEED = register("speed", () -> new Spell(SpellType.HELD, SpellActions::doSpeed, "Speed", 1, 1));
    public static final RegistryObject<Spell> PIERCING_BOLT = register("piercing_bolt", () -> new Spell(SpellType.CAST, SpellActions::doPiercingBolt, "Piercing Bolt", 12));
    public static final RegistryObject<Spell> ABSORBING = register("absorbing", () -> new Spell(SpellType.CAST, SpellActions::doAbsorbing, "Absorbing", 6));
    public static final RegistryObject<Spell> FANGS = register("fangs", () -> new Spell(SpellType.CAST, SpellActions::doFangsSpell, "Fangs", 6));
    public static final RegistryObject<Spell> FLAMING_BOLT = register("flaming_bolt", () -> new Spell(SpellType.CAST, SpellActions::doFlamingBolt, "Flaming Bolt", 7));
    public static final RegistryObject<Spell> AQUA_BOLT = register("aqua_bolt", () -> new Spell(SpellType.CAST, SpellActions::doAquaBolt, "Aqua Bolt", 4));
    public static final RegistryObject<Spell> LURE = register("lure", () -> new Spell(SpellType.HELD, SpellActions::doLure, "Lure", 1, 10));
    public static final RegistryObject<Spell> REPEL = register("repel", () -> new Spell(SpellType.HELD, SpellActions::doRepel, "Repel", 1, 3));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new Spell(SpellType.HELD, SpellActions::doFlamingCircle, "Flaming Circle", 1, 6));
    public static final RegistryObject<Spell> FROST_PATH =  register("frost_path", () -> new Spell(SpellType.HELD, SpellActions::doFrostPath, "Frost Path", 1, 6));

    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry("spell", () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) -> {})
                    .setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty")));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        RegistryObject<Spell> registryObject = SPELLS_DEFERRED_REGISTER.register(name, spell);
        HLSpells.LOGGER.info("Spell added : " + spell.get().getTrueDisplayName() + " ");
        return registryObject;
    }
}
