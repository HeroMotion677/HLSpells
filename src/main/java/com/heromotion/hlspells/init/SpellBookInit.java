package com.heromotion.hlspells.init;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.spell.*;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellBookInit {

    public static final DeferredRegister<SpellBook> SPELL_BOOK_DEFERRED_REGISTER = DeferredRegister.create(SpellBook.class, HLSpells.MODID);

    public static Supplier<IForgeRegistry<SpellBook>> SPELL_BOOK_REGISTRY = SPELL_BOOK_DEFERRED_REGISTER.makeRegistry("spell_book", () ->
            new RegistryBuilder<SpellBook>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell Book added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<SpellBook> EMPTY = SPELL_BOOK_DEFERRED_REGISTER.register("empty", SpellBook::new);
    public static final RegistryObject<SpellBook> SLOW_FALLING = SPELL_BOOK_DEFERRED_REGISTER.register("slow_falling", () -> new SpellBook(new SpellInstance(SpellInit.SLOW_FALLING.get())));
    public static final RegistryObject<SpellBook> BLAST_PROTECTION = SPELL_BOOK_DEFERRED_REGISTER.register("blast_protection", () -> new SpellBook(new SpellInstance(SpellInit.BLAST_PROTECTION.get())));
    public static final RegistryObject<SpellBook> SOUL_SYPHON = SPELL_BOOK_DEFERRED_REGISTER.register("soul_syphon", () -> new SpellBook(new SpellInstance(SpellInit.SOUL_SYPHON.get())));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}
