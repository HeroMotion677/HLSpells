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
                    HLSpells.LOGGER.info("Spell Book added: " + getName(obj).toString() + " ")).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty")));

    public static final RegistryObject<SpellBookObject> EMPTY = SPELL_BOOK_DEFERRED_REGISTER.register("empty", () -> new SpellBookObject(SpellInit.EMPTY.get().getTrueDisplayName(), new SpellInstance(SpellInit.EMPTY.get())));
    public static final RegistryObject<SpellBookObject> FEATHER_FALLING = SPELL_BOOK_DEFERRED_REGISTER.register("feather_falling", () -> new SpellBookObject(SpellInit.FEATHER_FALLING.get().getTrueDisplayName(), new SpellInstance(SpellInit.FEATHER_FALLING.get())));
    public static final RegistryObject<SpellBookObject> BLAST_PROTECTION = SPELL_BOOK_DEFERRED_REGISTER.register("blast_protection", () -> new SpellBookObject(SpellInit.BLAST_PROTECTION.get().getTrueDisplayName(), new SpellInstance(SpellInit.BLAST_PROTECTION.get())));
    public static final RegistryObject<SpellBookObject> SOUL_SYPHON = SPELL_BOOK_DEFERRED_REGISTER.register("soul_syphon", () -> new SpellBookObject(SpellInit.SOUL_SYPHON.get().getTrueDisplayName(), new SpellInstance(SpellInit.SOUL_SYPHON.get())));
    public static final RegistryObject<SpellBookObject> ARROW_RAIN = SPELL_BOOK_DEFERRED_REGISTER.register("arrow_rain", () -> new SpellBookObject(SpellInit.ARROW_RAIN.get().getTrueDisplayName(), new SpellInstance(SpellInit.ARROW_RAIN.get())));
    public static final RegistryObject<SpellBookObject> BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("bolt", () -> new SpellBookObject(SpellInit.BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.BOLT.get())));
    public static final RegistryObject<SpellBookObject> HEALING_CIRCLE = SPELL_BOOK_DEFERRED_REGISTER.register("healing_circle", () -> new SpellBookObject(SpellInit.HEALING_CIRCLE.get().getTrueDisplayName(), new SpellInstance(SpellInit.HEALING_CIRCLE.get())));
    public static final RegistryObject<SpellBookObject> SOUL_SUMMON = SPELL_BOOK_DEFERRED_REGISTER.register("soul_summon", () -> new SpellBookObject(SpellInit.SOUL_SUMMON.get().getTrueDisplayName(), new SpellInstance(SpellInit.SOUL_SUMMON.get())));
    public static final RegistryObject<SpellBookObject> PULL = SPELL_BOOK_DEFERRED_REGISTER.register("pull", () -> new SpellBookObject(SpellInit.PULL.get().getTrueDisplayName(), new SpellInstance(SpellInit.PULL.get())));
    public static final RegistryObject<SpellBookObject> BOND = SPELL_BOOK_DEFERRED_REGISTER.register("bond",  () -> new SpellBookObject(SpellInit.BOND.get().getTrueDisplayName(), new SpellInstance(SpellInit.BOND.get())));
    public static final RegistryObject<SpellBookObject> STORM = SPELL_BOOK_DEFERRED_REGISTER.register("storm", () -> new SpellBookObject(SpellInit.STORM.get().getTrueDisplayName(), new SpellInstance(SpellInit.STORM.get())));
    public static final RegistryObject<SpellBookObject> FIRE_BALL = SPELL_BOOK_DEFERRED_REGISTER.register("fire_ball", () -> new SpellBookObject(SpellInit.FIRE_BALL.get().getTrueDisplayName(), new SpellInstance(SpellInit.FIRE_BALL.get())));
    public static final RegistryObject<SpellBookObject> LEVITATION = SPELL_BOOK_DEFERRED_REGISTER.register("levitation", () -> new SpellBookObject(SpellInit.LEVITATION.get().getTrueDisplayName(), new SpellInstance(SpellInit.LEVITATION.get())));
    public static final RegistryObject<SpellBookObject> LIGHTNING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("lightning_bolt", () -> new SpellBookObject(SpellInit.LIGHTNING_BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.LIGHTNING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> KNOCKBACK_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("knockback_bolt", () -> new SpellBookObject(SpellInit.KNOCKBACK_BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.KNOCKBACK_BOLT.get())));
    public static final RegistryObject<SpellBookObject> PROTECTION_CIRCLE = SPELL_BOOK_DEFERRED_REGISTER.register("protection_circle", () -> new SpellBookObject(SpellInit.PROTECTION_CIRCLE.get().getTrueDisplayName(), new SpellInstance(SpellInit.PROTECTION_CIRCLE.get())));
    public static final RegistryObject<SpellBookObject> RESPIRATION = SPELL_BOOK_DEFERRED_REGISTER.register("respiration", () -> new SpellBookObject(SpellInit.RESPIRATION.get().getTrueDisplayName(), new SpellInstance(SpellInit.RESPIRATION.get())));
    public static final RegistryObject<SpellBookObject> SPEED = SPELL_BOOK_DEFERRED_REGISTER.register("speed",() -> new SpellBookObject(SpellInit.SPEED.get().getTrueDisplayName(), new SpellInstance( SpellInit.SPEED.get())));
    public static final RegistryObject<SpellBookObject> PIERCING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("piercing_bolt", () -> new SpellBookObject(SpellInit.PIERCING_BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.PIERCING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> ABSORBING = SPELL_BOOK_DEFERRED_REGISTER.register("absorbing", () -> new SpellBookObject(SpellInit.ABSORBING.get().getTrueDisplayName(), new SpellInstance(SpellInit.ABSORBING.get())));
    public static final RegistryObject<SpellBookObject> FANGS = SPELL_BOOK_DEFERRED_REGISTER.register("fangs", () -> new SpellBookObject(SpellInit.FANGS.get().getTrueDisplayName(), new SpellInstance(SpellInit.FANGS.get())));
    public static final RegistryObject<SpellBookObject> FLAMING_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("flaming_bolt", () -> new SpellBookObject(SpellInit.FLAMING_BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.FLAMING_BOLT.get())));
    public static final RegistryObject<SpellBookObject> AQUA_BOLT = SPELL_BOOK_DEFERRED_REGISTER.register("aqua_bolt", () -> new SpellBookObject(SpellInit.AQUA_BOLT.get().getTrueDisplayName(), new SpellInstance(SpellInit.AQUA_BOLT.get())));
    public static final RegistryObject<SpellBookObject> LURE = SPELL_BOOK_DEFERRED_REGISTER.register("lure", () -> new SpellBookObject(SpellInit.LURE.get().getTrueDisplayName(), new SpellInstance(SpellInit.LURE.get())));
    public static final RegistryObject<SpellBookObject> REPEL = SPELL_BOOK_DEFERRED_REGISTER.register("repel", () -> new SpellBookObject(SpellInit.REPEL.get().getTrueDisplayName(), new SpellInstance(SpellInit.REPEL.get())));
    public static final RegistryObject<SpellBookObject> FLAMING_CIRCLE = SPELL_BOOK_DEFERRED_REGISTER.register("flaming_circle", () -> new SpellBookObject(SpellInit.FLAMING_CIRCLE.get().getTrueDisplayName(), new SpellInstance(SpellInit.FLAMING_CIRCLE.get())));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}