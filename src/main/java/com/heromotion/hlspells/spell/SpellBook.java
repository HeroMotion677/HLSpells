package com.heromotion.hlspells.spell;

import com.google.common.collect.ImmutableList;

import com.heromotion.hlspells.init.SpellBookInit;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;

public class SpellBook extends ForgeRegistryEntry<SpellBook> {

    private final String name;
    private final ImmutableList<SpellInstance> spellInstances;

    public SpellBook(SpellInstance... spellInstances) {
        this(null, spellInstances);
    }

    public SpellBook(@Nullable String name, SpellInstance... spellInstances) {
        this.name = name;
        this.spellInstances = ImmutableList.copyOf(spellInstances);
    }

    public static SpellBook byName(String name) {
        return SpellBookInit.SPELL_BOOK_REGISTRY.get().getValue(ResourceLocation.tryParse(name));
    }

    public boolean isIn(ITag<SpellBook> tag) {
        return tag.contains(this);
    }

    public String getName(String name) {
        if (SpellBookInit.SPELL_BOOK_REGISTRY.get().getKey(this) == null) return "";
        return name + (this.name == null ? SpellBookInit.SPELL_BOOK_REGISTRY.get().getKey(this).getPath() : this.name);
    }

    public List<SpellInstance> getSpells() {
        return this.spellInstances;
    }

    public boolean hasInstantAlterations() {
        if (!this.spellInstances.isEmpty()) {
            for (SpellInstance spellInstance : this.spellInstances) {
                if (spellInstance.getSpell().isInstantenous()) {
                    return true;
                }
            }
        }

        return false;
    }
}
