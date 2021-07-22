package com.divinity.hlspells.init;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.SpellType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellInit
{
    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(Spell.class, HLSpells.MODID);
    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry("spell", () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<Spell> EMPTY = SPELLS_DEFERRED_REGISTER.register("empty", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> FEATHER_FALLING = SPELLS_DEFERRED_REGISTER.register("feather_falling", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> BLAST_PROTECTION = SPELLS_DEFERRED_REGISTER.register("blast_protection", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> SOUL_SYPHON = SPELLS_DEFERRED_REGISTER.register("soul_syphon", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> ARROW_RAIN = SPELLS_DEFERRED_REGISTER.register("arrow_rain", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> BOLT = SPELLS_DEFERRED_REGISTER.register("bolt", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> HEALING_CIRCLE = SPELLS_DEFERRED_REGISTER.register("healing_circle", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> SOUL_SUMMON = SPELLS_DEFERRED_REGISTER.register("soul_summon", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> PULL = SPELLS_DEFERRED_REGISTER.register("pull", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> BOND = SPELLS_DEFERRED_REGISTER.register("bond", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> STORM = SPELLS_DEFERRED_REGISTER.register("storm", () -> new  Spell(SpellType.CAST));
    public static final RegistryObject<Spell> FIRE_BALL = SPELLS_DEFERRED_REGISTER.register("fire_ball", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> LEVITATION = SPELLS_DEFERRED_REGISTER.register("levitation", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = SPELLS_DEFERRED_REGISTER.register("lightning_bolt", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = SPELLS_DEFERRED_REGISTER.register("knockback_bolt", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> PROTECTION_CIRCLE = SPELLS_DEFERRED_REGISTER.register("protection_circle", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> RESPIRATION = SPELLS_DEFERRED_REGISTER.register("respiration", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> SPEED = SPELLS_DEFERRED_REGISTER.register("speed", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> PIERCING_BOLT = SPELLS_DEFERRED_REGISTER.register("piercing_bolt", () -> new Spell(SpellType.CAST));
    public static final RegistryObject<Spell> ABSORBING = SPELLS_DEFERRED_REGISTER.register("absorbing", () -> new Spell(SpellType.HELD));
    public static final RegistryObject<Spell> FANGS = SPELLS_DEFERRED_REGISTER.register("fangs", () -> new Spell(SpellType.CAST));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type)
    {
        return Objects.requireNonNull(type.getRegistryName());
    }
}