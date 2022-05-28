package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

@SuppressWarnings("all")
public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(new ResourceLocation("spell"), HLSpells.MODID);

    public static final RegistryObject<Spell> EMPTY = register("empty", () -> new EmptySpell("No Spell", 0, false));
    public static final RegistryObject<Spell> SLOW_FALL = register("slow_fall", () -> new SlowFallSpell("Slow Falling", 1, 15, false));
    public static final RegistryObject<Spell> BLAST = register("blast", () -> new BlastSpell("Blast", 6, false));
    public static final RegistryObject<Spell> TELEPORT = register("teleport", () -> new TeleportSpell("Teleport", 9, false));
    public static final RegistryObject<Spell> ARROW_RAIN = register("arrow_rain", () -> new ArrowRainSpell("Arrow Rain", 1, 6, false));
    public static final RegistryObject<Spell> BOLT = register("bolt", () -> new BoltSpell("Bolt", 5, false));
    public static final RegistryObject<Spell> HEALING_CIRCLE = register("healing_circle", () -> new HealingCircleSpell("Healing Circle", 1, 3, false));
    public static final RegistryObject<Spell> SOUL_SUMMON = register("soul_summon", () -> new SoulSummonSpell("Soul Summon", 16, true));
    public static final RegistryObject<Spell> PULL = register("pull", () -> new PullSpell( "Pull", 3, false));
    public static final RegistryObject<Spell> BOND = register("bond", () -> new BondSpell("Bond", 1, false));
    public static final RegistryObject<Spell> LIGHTNING_CHAIN = register("lightning_chain", () -> new LightningChainSpell("Lightning Chain", 14, false));
    public static final RegistryObject<Spell> FIRE_BALL = register("fire_ball", () -> new FireballSpell("Fire Ball", 5, false));
    public static final RegistryObject<Spell> LEVITATION = register("levitation", () -> new LevitationSpell("Levitation", 1, 6, false));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = register("lightning_bolt", () -> new LightningBoltSpell("Lightning Bolt", 8, false));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = register("knockback_bolt", () -> new KnockbackBoltSpell("Knockback Bolt", 3, false));
    public static final RegistryObject<Spell> SPEED = register("speed", () -> new SpeedSpell("Speed", 1, 1, false));
    public static final RegistryObject<Spell> PIERCING_BOLT = register("piercing_bolt", () -> new PiercingBoltSpell("Piercing Bolt", 12, false));
    public static final RegistryObject<Spell> ABSORBING = register("absorbing", () -> new AbsorbingSpell("Absorbing", 6, false));
    public static final RegistryObject<Spell> FANGS = register("fangs", () -> new FangsSpell("Fangs", 6, true));
    public static final RegistryObject<Spell> FLAMING_BOLT = register("flaming_bolt", () -> new FlamingBoltSpell("Flaming Bolt", 7, false));
    public static final RegistryObject<Spell> AQUA_BOLT = register("aqua_bolt", () -> new AquaBoltSpell("Aqua Bolt", 4, false));
    public static final RegistryObject<Spell> LURE = register("lure", () -> new LureSpell( "Lure", 1, 10, false));
    public static final RegistryObject<Spell> FROST_PATH =  register("frost_path", () -> new FrostPathSpell("Frost Path", 1, 6, false));
    public static final RegistryObject<Spell> WITHER_SKULL = register("wither_skull", () -> new WitherSkullSpell("Wither Skull", 10, false));
    public static final RegistryObject<Spell> TORPEDO = register("torpedo", () -> new TorpedoSpell("Torpedo", 8, false));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new FlamingCircleSpell("Flaming Circle", 1, 6, false));
    public static final RegistryObject<Spell> RESPIRATION = register("respiration", () -> new RespirationSpell("Respiration", 1, 15, false));

    //public static final RegistryObject<Spell> REPEL = register("repel", () -> new Spell(SpellAttributes.HELD, SpellActions::doRepel, "Repel", 1, 3));
    //public static final RegistryObject<Spell> PROTECTION_CIRCLE = register("protection_circle", () -> new Spell(SpellAttributes.HELD, SpellActions::doProtectionCircle, "Protection Circle", 1, 6));

    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS.makeRegistry(Spell.class,
            () -> new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1)
                    .onAdd((owner, stage, id, obj, oldObj) -> {})
                    .setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty")));

    public static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        RegistryObject<Spell> registryObject = SPELLS.register(name, spell);
        HLSpells.LOGGER.info("Spell added : " + spell.get().getTrueDisplayName() + " ");
        return registryObject;
    }
}
