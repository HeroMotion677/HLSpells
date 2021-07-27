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
import org.lwjgl.system.CallbackI;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellBookInit {

    public static final DeferredRegister<SpellBookObject> SPELL_BOOK_DEFERRED_REGISTER = DeferredRegister.create(SpellBookObject.class, HLSpells.MODID);

    public static Supplier<IForgeRegistry<SpellBookObject>> SPELL_BOOK_REGISTRY = SPELL_BOOK_DEFERRED_REGISTER.makeRegistry("spell_book", () ->
            new RegistryBuilder<SpellBookObject>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell Book added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<SpellBookObject> EMPTY = SPELL_BOOK_DEFERRED_REGISTER.register("empty", SpellBookObject::new);
    public static final RegistryObject<SpellBookObject> FEATHER_FALLING = SPELL_BOOK_DEFERRED_REGISTER.register("feather_falling", () -> new SpellBookObject(new SpellInstance(SpellInit.FEATHER_FALLING.get())));
    public static final RegistryObject<SpellBookObject> BLAST_PROTECTION = SPELL_BOOK_DEFERRED_REGISTER.register("blast_protection", () -> new SpellBookObject(new SpellInstance(SpellInit.BLAST_PROTECTION.get())));
    public static final RegistryObject<SpellBookObject> SOUL_SYPHON = SPELL_BOOK_DEFERRED_REGISTER.register("soul_syphon", () -> new SpellBookObject(new SpellInstance(SpellInit.SOUL_SYPHON.get())));
    public static final RegistryObject<SpellBookObject> ARROW_RAIN = SPELL_BOOK_DEFERRED_REGISTER.register("arrow_rain", () -> new SpellBookObject(new SpellInstance(SpellInit.ARROW_RAIN.get())));
    public static final RegistryObject<SpellBookObject> BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.BOLT.get())));
    public static final RegistryObject<SpellBookObject> HEALING_CIRCLE = SPELL_BOOK_DEFERRED_REGISTER.register("healing_circle", () -> new SpellBookObject(new SpellInstance(SpellInit.HEALING_CIRCLE.get())));
    public static final RegistryObject<SpellBookObject> SOUL_SUMMON = SPELL_BOOK_DEFERRED_REGISTER.register("soul_summon", () -> new SpellBookObject(new SpellInstance(SpellInit.SOUL_SUMMON.get())));
    public static final RegistryObject<SpellBookObject> PULL = SPELL_BOOK_DEFERRED_REGISTER.register("pull", () -> new SpellBookObject(new SpellInstance(SpellInit.PULL.get())));
    public static final RegistryObject<SpellBookObject> BOND = SPELL_BOOK_DEFERRED_REGISTER.register("bond",  () -> new SpellBookObject(new SpellInstance(SpellInit.BOND.get())));
    public static final RegistryObject<SpellBookObject> STORM = SPELL_BOOK_DEFERRED_REGISTER.register("storm", () -> new SpellBookObject(new SpellInstance(SpellInit.STORM.get())));
    public static final RegistryObject<SpellBookObject> FIRE_BALL = SPELL_BOOK_DEFERRED_REGISTER.register("fire_ball", () -> new SpellBookObject(new SpellInstance(SpellInit.FIRE_BALL.get())));
    public static final RegistryObject<SpellBookObject> LEVITATION = SPELL_BOOK_DEFERRED_REGISTER.register("levitation", () -> new SpellBookObject(new SpellInstance(SpellInit.LEVITATION.get())));
    public static final RegistryObject<SpellBookObject> LIGHTNING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("lightning_bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.LIGHTNING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> KNOCKBACK_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("knockback_bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.KNOCKBACK_BOLT.get())));
    public static final RegistryObject<SpellBookObject> PROTECTION_CIRCLE = SPELL_BOOK_DEFERRED_REGISTER.register("protection_circle", () -> new SpellBookObject(new SpellInstance(SpellInit.PROTECTION_CIRCLE.get())));
    public static final RegistryObject<SpellBookObject> RESPIRATION = SPELL_BOOK_DEFERRED_REGISTER.register("respiration", () -> new SpellBookObject(new SpellInstance(SpellInit.RESPIRATION.get())));
    public static final RegistryObject<SpellBookObject> SPEED = SPELL_BOOK_DEFERRED_REGISTER.register("speed", () -> new SpellBookObject(new SpellInstance(SpellInit.SPEED.get())));
    public static final RegistryObject<SpellBookObject> PIERCING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("piercing_bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.PIERCING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> ABSORBING = SPELL_BOOK_DEFERRED_REGISTER.register("absorbing", () -> new SpellBookObject(new SpellInstance(SpellInit.ABSORBING.get())));
    public static final RegistryObject<SpellBookObject> FANGS = SPELL_BOOK_DEFERRED_REGISTER.register("fangs", () -> new SpellBookObject(new SpellInstance(SpellInit.FANGS.get())));
    public static final RegistryObject<SpellBookObject> FLAMING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("flaming_bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.FLAMING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> AQUA_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("aqua_bolt", () -> new SpellBookObject(new SpellInstance(SpellInit.AQUA_BOLT.get())));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}