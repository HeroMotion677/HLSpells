package com.divinity.hlspells.init;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellInstance;
import com.divinity.hlspells.HLSpells;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellBookInit {

    public static final DeferredRegister<SpellBookObject> SPELL_BOOK_DEFERRED_REGISTER = DeferredRegister.create(SpellBookObject.class, HLSpells.MODID);

    public static Supplier<IForgeRegistry<SpellBookObject>> SPELL_BOOK_REGISTRY = SPELL_BOOK_DEFERRED_REGISTER.makeRegistry("spell_book", () ->
            new RegistryBuilder<SpellBookObject>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell Book added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<SpellBookObject> EMPTY = register("empty", SpellInit.EMPTY.get());
    public static final RegistryObject<SpellBookObject> FEATHER_FALLING = register("feather_falling", SpellInit.FEATHER_FALLING.get());
    public static final RegistryObject<SpellBookObject> BLAST_PROTECTION = register("blast_protection", SpellInit.BLAST_PROTECTION.get());
    public static final RegistryObject<SpellBookObject> SOUL_SYPHON = register("soul_syphon", SpellInit.SOUL_SYPHON.get());
    public static final RegistryObject<SpellBookObject> ARROW_RAIN = register("arrow_rain", SpellInit.ARROW_RAIN.get());
    public static final RegistryObject<SpellBookObject> BOLT = register("bolt", SpellInit.BOLT.get());
    public static final RegistryObject<SpellBookObject> HEALING_CIRCLE = register("healing_circle", SpellInit.HEALING_CIRCLE.get());
    public static final RegistryObject<SpellBookObject> SOUL_SUMMON = register("soul_summon", SpellInit.SOUL_SUMMON.get());
    public static final RegistryObject<SpellBookObject> PULL = register("pull", SpellInit.PULL.get());
    public static final RegistryObject<SpellBookObject> BOND = register("bond",  SpellInit.BOND.get());
    public static final RegistryObject<SpellBookObject> STORM = register("storm", SpellInit.STORM.get());
    public static final RegistryObject<SpellBookObject> FIRE_BALL = register("fire_ball", SpellInit.FIRE_BALL.get());
    public static final RegistryObject<SpellBookObject> LEVITATION = register("levitation", SpellInit.LEVITATION.get());
    public static final RegistryObject<SpellBookObject> LIGHTNING_BOLT = register("lightning_bolt", SpellInit.LIGHTNING_BOLT.get());
    public static final RegistryObject<SpellBookObject> KNOCKBACK_BOLT = register("knockback_bolt", SpellInit.KNOCKBACK_BOLT.get());
    public static final RegistryObject<SpellBookObject> PROTECTION_CIRCLE = register("protection_circle", SpellInit.PROTECTION_CIRCLE.get());
    public static final RegistryObject<SpellBookObject> RESPIRATION = register("respiration", SpellInit.RESPIRATION.get());
    public static final RegistryObject<SpellBookObject> SPEED = register("speed", SpellInit.SPEED.get());
    public static final RegistryObject<SpellBookObject> PIERCING_BOLT = register("piercing_bolt", SpellInit.PIERCING_BOLT.get());
    public static final RegistryObject<SpellBookObject> ABSORBING = register("absorbing", SpellInit.ABSORBING.get());
    public static final RegistryObject<SpellBookObject> FANGS = register("fangs", SpellInit.FANGS.get());
    public static final RegistryObject<SpellBookObject> FLAMING_BOLT = register("flaming_bolt", SpellInit.FLAMING_BOLT.get());
    public static final RegistryObject<SpellBookObject> AQUA_BOLT = register("aqua_bolt", SpellInit.AQUA_BOLT.get());

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<SpellBookObject> register(String name, Spell spell) {
        return SPELL_BOOK_DEFERRED_REGISTER.register(name, () -> new SpellBookObject(new SpellInstance(spell)));
    }
}