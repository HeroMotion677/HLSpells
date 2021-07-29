package com.divinity.hlspells.init;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import java.util.Objects;
import java.util.function.Supplier;

import static com.divinity.hlspells.spells.SpellActions.*;

public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(Spell.class, HLSpells.MODID);
    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry("spell", () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<Spell> EMPTY = SPELLS_DEFERRED_REGISTER.register("empty", () -> new Spell(SpellType.CAST, (p, w) -> {}));
    public static final RegistryObject<Spell> FEATHER_FALLING = SPELLS_DEFERRED_REGISTER.register("feather_falling", () -> new Spell(SpellType.HELD, (p, w) -> doFeatherFalling(p)));
    public static final RegistryObject<Spell> BLAST_PROTECTION = SPELLS_DEFERRED_REGISTER.register("blast_protection", () -> new Spell(SpellType.CAST, (p, w) -> doBlastSpell(w, p)));
    public static final RegistryObject<Spell> SOUL_SYPHON = SPELLS_DEFERRED_REGISTER.register("soul_syphon", () -> new Spell(SpellType.CAST, (p, w) -> doSoulSyphon(w, p)));
    public static final RegistryObject<Spell> ARROW_RAIN = SPELLS_DEFERRED_REGISTER.register("arrow_rain", () -> new Spell(SpellType.HELD, (p, w) -> doArrowRain(p)));
    public static final RegistryObject<Spell> BOLT = SPELLS_DEFERRED_REGISTER.register("bolt", () -> new Spell(SpellType.CAST, (p, w) -> doBoltSpell(p)));
    public static final RegistryObject<Spell> HEALING_CIRCLE = SPELLS_DEFERRED_REGISTER.register("healing_circle", () -> new Spell(SpellType.HELD, (p, w) -> doHealingCircle(p)));
    public static final RegistryObject<Spell> SOUL_SUMMON = SPELLS_DEFERRED_REGISTER.register("soul_summon", () -> new Spell(SpellType.CAST, (p, w) -> doSummonSpell(p)));
    public static final RegistryObject<Spell> PULL = SPELLS_DEFERRED_REGISTER.register("pull", () -> new Spell(SpellType.CAST, (p, w) -> doPullSpell(p)));
    public static final RegistryObject<Spell> BOND = SPELLS_DEFERRED_REGISTER.register("bond", () -> new Spell(SpellType.CAST, (p, w) -> doTameSpell(p)));
    public static final RegistryObject<Spell> STORM = SPELLS_DEFERRED_REGISTER.register("storm", () -> new  Spell(SpellType.CAST, (p, w) -> doStormSpell(p)));
    public static final RegistryObject<Spell> FIRE_BALL = SPELLS_DEFERRED_REGISTER.register("fire_ball", () -> new Spell(SpellType.CAST, (p, w) -> doFireBallSpell(p)));
    public static final RegistryObject<Spell> LEVITATION = SPELLS_DEFERRED_REGISTER.register("levitation", () -> new Spell(SpellType.HELD, (p, w) -> doLevitation(p)));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = SPELLS_DEFERRED_REGISTER.register("lightning_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doLightningBolt(p)));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = SPELLS_DEFERRED_REGISTER.register("knockback_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doKnockBackBolt(p)));
    public static final RegistryObject<Spell> PROTECTION_CIRCLE = SPELLS_DEFERRED_REGISTER.register("protection_circle", () -> new Spell(SpellType.HELD, (p, w) -> doProtectionCircle(p)));
    public static final RegistryObject<Spell> RESPIRATION = SPELLS_DEFERRED_REGISTER.register("respiration", () -> new Spell(SpellType.HELD, (p, w) -> doRespiration(p)));
    public static final RegistryObject<Spell> SPEED = SPELLS_DEFERRED_REGISTER.register("speed", () -> new Spell(SpellType.HELD, (p, w) -> doSpeed(p)));
    public static final RegistryObject<Spell> PIERCING_BOLT = SPELLS_DEFERRED_REGISTER.register("piercing_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doPiercingBolt(p)));
    public static final RegistryObject<Spell> ABSORBING = SPELLS_DEFERRED_REGISTER.register("absorbing", () -> new Spell(SpellType.CAST, (p, w) -> doAbsorbing(p)));
    public static final RegistryObject<Spell> FANGS = SPELLS_DEFERRED_REGISTER.register("fangs", () -> new Spell(SpellType.CAST, (p, w) -> doFangsSpell(p)));
    public static final RegistryObject<Spell> FLAMING_BOLT = SPELLS_DEFERRED_REGISTER.register("flaming_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doFlamingBolt(p)));
    public static final RegistryObject<Spell> AQUA_BOLT  = SPELLS_DEFERRED_REGISTER.register("aqua_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doAquaBolt(p)));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        return SPELLS_DEFERRED_REGISTER.register(name,spell);
    }
}
