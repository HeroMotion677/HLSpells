package com.heromotion.hlspells.init;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.spell.*;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(Spell.class, HLSpells.MODID);

    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry("spell", () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) ->
                    HLSpells.LOGGER.info("Spell added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty"))
    );

    public static final RegistryObject<Spell> EMPTY = SPELLS_DEFERRED_REGISTER.register("empty", () -> new Spell(SpellType.NORMAL));
    public static final RegistryObject<Spell> FEATHER_FALLING = SPELLS_DEFERRED_REGISTER.register("feather_falling", () -> new Spell(SpellType.NORMAL));
    public static final RegistryObject<Spell> BLAST_PROTECTION = SPELLS_DEFERRED_REGISTER.register("blast_protection", () -> new Spell(SpellType.NORMAL));
    public static final RegistryObject<Spell> SOUL_SYPHON = SPELLS_DEFERRED_REGISTER.register("soul_syphon", () -> new Spell(SpellType.NORMAL));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }
}
