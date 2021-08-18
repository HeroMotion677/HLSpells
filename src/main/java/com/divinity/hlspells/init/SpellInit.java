package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;
import java.util.function.Supplier;

import static com.divinity.hlspells.spells.SpellActions.*;

public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(Spell.class, HLSpells.MODID);
    public static final RegistryObject<Spell> EMPTY = register("empty", () -> new Spell(SpellType.CAST, (p, w) -> {
    }, "No Spell"));
    public static final RegistryObject<Spell> FEATHER_FALLING = register("feather_falling", () -> new Spell(SpellType.HELD, (p, w) -> doFeatherFalling(p), "Feather Falling", 1, 15));
    public static final RegistryObject<Spell> BLAST_PROTECTION = register("blast_protection", () -> new Spell(SpellType.CAST, (p, w) -> doBlastSpell(w, p), "Blast Protection", 6));
    public static final RegistryObject<Spell> SOUL_SYPHON = register("soul_syphon", () -> new Spell(SpellType.CAST, (p, w) -> doSoulSyphon(w, p), "Teleport", 9));
    public static final RegistryObject<Spell> ARROW_RAIN = register("arrow_rain", () -> new Spell(SpellType.HELD, (p, w) -> doArrowRain(p), "Arrow Rain", 1, 6));
    public static final RegistryObject<Spell> BOLT = register("bolt", () -> new Spell(SpellType.CAST, (p, w) -> doBoltSpell(p), "Bolt", 5));
    public static final RegistryObject<Spell> HEALING_CIRCLE = register("healing_circle", () -> new Spell(SpellType.HELD, (p, w) -> doHealingCircle(p), "Healing Circle", 1, 3));
    public static final RegistryObject<Spell> SOUL_SUMMON = register("soul_summon", () -> new Spell(SpellType.CAST, (p, w) -> doSummonSpell(p), "Soul Summon", 16));
    public static final RegistryObject<Spell> PULL = register("pull", () -> new Spell(SpellType.CAST, (p, w) -> doPullSpell(p), "Pull", 3));
    public static final RegistryObject<Spell> BOND = register("bond", () -> new Spell(SpellType.CAST, (p, w) -> doBondSpell(p), "Bond", 1));
    public static final RegistryObject<Spell> STORM = register("storm", () -> new Spell(SpellType.CAST, (p, w) -> doStormSpell(p), "Storm", 14));
    public static final RegistryObject<Spell> FIRE_BALL = register("fire_ball", () -> new Spell(SpellType.CAST, (p, w) -> doFireBallSpell(p), "Fire Ball", 5));
    public static final RegistryObject<Spell> LEVITATION = register("levitation", () -> new Spell(SpellType.HELD, (p, w) -> doLevitation(p), "Levitation", 1, 6));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = register("lightning_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doLightningBolt(p), "Lightning Bolt", 8));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = register("knockback_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doKnockBackBolt(p), "Knockback Bolt", 3));
    public static final RegistryObject<Spell> PROTECTION_CIRCLE = register("protection_circle", () -> new Spell(SpellType.HELD, (p, w) -> doProtectionCircle(p), "Protection Circle"));
    public static final RegistryObject<Spell> RESPIRATION = register("respiration", () -> new Spell(SpellType.HELD, (p, w) -> doRespiration(p), "Respiration", 1, 15));
    public static final RegistryObject<Spell> SPEED = register("speed", () -> new Spell(SpellType.HELD, (p, w) -> doSpeed(p), "Speed"));
    public static final RegistryObject<Spell> PIERCING_BOLT = register("piercing_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doPiercingBolt(p), "Piercing Bolt", 12));
    public static final RegistryObject<Spell> ABSORBING = register("absorbing", () -> new Spell(SpellType.CAST, (p, w) -> doAbsorbing(p), "Absorbing", 6));
    public static final RegistryObject<Spell> FANGS = register("fangs", () -> new Spell(SpellType.CAST, (p, w) -> doFangsSpell(p), "Fangs", 6));
    public static final RegistryObject<Spell> FLAMING_BOLT = register("flaming_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doFlamingBolt(p), "Flaming Bolt", 7));
    public static final RegistryObject<Spell> AQUA_BOLT = register("aqua_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doAquaBolt(p), "Aqua Bolt", 4));
    public static final RegistryObject<Spell> LURE = register("lure", () -> new Spell(SpellType.HELD, (p, w) -> doLure(p), "Lure", 1, 10));
    public static final RegistryObject<Spell> REPEL = register("repel", () -> new Spell(SpellType.HELD, (p, w) -> doRepel(p), "Repel", 1, 3));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new Spell(SpellType.HELD, (p, w) -> doFlamingCircle(p), "Flaming Circle", 1, 6));
    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry("spell", () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        return SPELLS_DEFERRED_REGISTER.register(name, spell);
    }
}
