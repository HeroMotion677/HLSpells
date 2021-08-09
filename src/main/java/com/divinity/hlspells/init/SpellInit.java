package com.divinity.hlspells.init;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AdvancementEvent;
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

    public static final RegistryObject<Spell> EMPTY = SPELLS_DEFERRED_REGISTER.register("empty", () -> new Spell(SpellType.CAST, (p, w) -> {}, "No Spell"));
    public static final RegistryObject<Spell> FEATHER_FALLING = SPELLS_DEFERRED_REGISTER.register("feather_falling", () -> new Spell(SpellType.HELD, (p, w) -> doFeatherFalling(p),"Feather Falling"));
    public static final RegistryObject<Spell> BLAST_PROTECTION = SPELLS_DEFERRED_REGISTER.register("blast_protection", () -> new Spell(SpellType.CAST, (p, w) -> doBlastSpell(w, p), "Blast Protection"));
    public static final RegistryObject<Spell> SOUL_SYPHON = SPELLS_DEFERRED_REGISTER.register("soul_syphon", () -> new Spell(SpellType.CAST, (p, w) -> doSoulSyphon(w, p),"Teleport"));
    public static final RegistryObject<Spell> ARROW_RAIN = SPELLS_DEFERRED_REGISTER.register("arrow_rain", () -> new Spell(SpellType.HELD, (p, w) -> doArrowRain(p), "Arrow Rain"));
    public static final RegistryObject<Spell> BOLT = SPELLS_DEFERRED_REGISTER.register("bolt", () -> new Spell(SpellType.CAST, (p, w) -> doBoltSpell(p), "Bolt"));
    public static final RegistryObject<Spell> HEALING_CIRCLE = SPELLS_DEFERRED_REGISTER.register("healing_circle", () -> new Spell(SpellType.HELD, (p, w) -> doHealingCircle(p), "Healing Circle"));
    public static final RegistryObject<Spell> SOUL_SUMMON = SPELLS_DEFERRED_REGISTER.register("soul_summon", () -> new Spell(SpellType.CAST, (p, w) -> doSummonSpell(p), "Soul Summon"));
    public static final RegistryObject<Spell> PULL = SPELLS_DEFERRED_REGISTER.register("pull", () -> new Spell(SpellType.CAST, (p, w) -> doPullSpell(p), "Pull"));
    public static final RegistryObject<Spell> BOND = SPELLS_DEFERRED_REGISTER.register("bond", () -> new Spell(SpellType.CAST, (p, w) -> doTameSpell(p), "Bond"));
    public static final RegistryObject<Spell> STORM = SPELLS_DEFERRED_REGISTER.register("storm", () -> new  Spell(SpellType.CAST, (p, w) -> doStormSpell(p), "Storm"));
    public static final RegistryObject<Spell> FIRE_BALL = SPELLS_DEFERRED_REGISTER.register("fire_ball", () -> new Spell(SpellType.CAST, (p, w) -> doFireBallSpell(p), "Fire Ball"));
    public static final RegistryObject<Spell> LEVITATION = SPELLS_DEFERRED_REGISTER.register("levitation", () -> new Spell(SpellType.HELD, (p, w) -> doLevitation(p), "Levitation"));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = SPELLS_DEFERRED_REGISTER.register("lightning_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doLightningBolt(p), "Lightning Bolt"));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = SPELLS_DEFERRED_REGISTER.register("knockback_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doKnockBackBolt(p), "Knockback Bolt"));
    public static final RegistryObject<Spell> PROTECTION_CIRCLE = SPELLS_DEFERRED_REGISTER.register("protection_circle", () -> new Spell(SpellType.HELD, (p, w) -> doProtectionCircle(p), "Protection Circle"));
    public static final RegistryObject<Spell> RESPIRATION = SPELLS_DEFERRED_REGISTER.register("respiration", () -> new Spell(SpellType.HELD, (p, w) -> doRespiration(p), "Respiration"));
    public static final RegistryObject<Spell> SPEED = SPELLS_DEFERRED_REGISTER.register("speed", () -> new Spell(SpellType.HELD, (p, w) -> doSpeed(p), "Speed"));
    public static final RegistryObject<Spell> PIERCING_BOLT = SPELLS_DEFERRED_REGISTER.register("piercing_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doPiercingBolt(p), "Piercing Bolt"));
    public static final RegistryObject<Spell> ABSORBING = SPELLS_DEFERRED_REGISTER.register("absorbing", () -> new Spell(SpellType.CAST, (p, w) -> doAbsorbing(p), "Absorbing"));
    public static final RegistryObject<Spell> FANGS = SPELLS_DEFERRED_REGISTER.register("fangs", () -> new Spell(SpellType.CAST, (p, w) -> doFangsSpell(p), "Fangs"));
    public static final RegistryObject<Spell> FLAMING_BOLT = SPELLS_DEFERRED_REGISTER.register("flaming_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doFlamingBolt(p), "Flaming Bolt"));
    public static final RegistryObject<Spell> AQUA_BOLT  = SPELLS_DEFERRED_REGISTER.register("aqua_bolt", () -> new Spell(SpellType.CAST, (p, w) -> doAquaBolt(p), "Aqua Bolt"));
    public static final RegistryObject<Spell> LURE = SPELLS_DEFERRED_REGISTER.register("lure", () -> new Spell(SpellType.HELD, (p, w) -> doLure(p), "Lure"));
    public static final RegistryObject<Spell> REPEL = SPELLS_DEFERRED_REGISTER.register("repel", () -> new Spell(SpellType.HELD, (p, w) -> doRepel(p), "Repel"));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = SPELLS_DEFERRED_REGISTER.register("flaming_circle", () -> new Spell(SpellType.HELD,  (p, w) -> doFlamingCircle(p), "Flaming Circle"));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type)
    {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static void register(String name, Supplier<Spell> spell)
    {
       SPELLS_DEFERRED_REGISTER.register(name,spell);
    }
}
