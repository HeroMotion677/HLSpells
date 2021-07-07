package com.heromotion.hlspells.init;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.spell.SpellBookObject;
import com.heromotion.hlspells.spell.SpellInstance;
import com.heromotion.hlspells.spell.SpellType;
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


    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}