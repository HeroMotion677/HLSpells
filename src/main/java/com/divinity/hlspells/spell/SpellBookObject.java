package com.divinity.hlspells.spell;

import com.divinity.hlspells.init.SpellBookInit;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class SpellBookObject extends ForgeRegistryEntry<SpellBookObject> {
    private final String name;
    private final ImmutableList<SpellInstance> spellInstances;

    public SpellBookObject(String name, SpellInstance... spellInstances) {
        this.name = name;
        this.spellInstances = ImmutableList.copyOf(spellInstances);
    }

    public static SpellBookObject byName(String name)
    {
        return SpellBookInit.SPELL_BOOK_REGISTRY.get().getValue(ResourceLocation.tryParse(name));
    }

    @Nullable
    public static SpellBookObject byId(String id) {
        return SpellBookInit.SPELL_BOOK_REGISTRY.get().getValue(new ResourceLocation(id));
    }

    @Nullable
    public static SpellBookObject byId(ResourceLocation id) {
        return SpellBookInit.SPELL_BOOK_REGISTRY.get().getValue(id);
    }

    public boolean isIn(ITag<SpellBookObject> tag) {
        return tag.contains(this);
    }

    public String getName() {
        return this.name;
    }

    public List<SpellInstance> getSpells() {
        return this.spellInstances;
    }

    public boolean hasInstantAlterations() {
        if (!this.spellInstances.isEmpty()) {
            for (SpellInstance spellInstance : this.spellInstances) {
                if (spellInstance.getSpell().isInstantaneous()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    public Spell getSpell() {
        return !spellInstances.isEmpty() ? spellInstances.get(0).getSpell() : null;
    }

    public boolean containsSpell(Predicate<SpellInstance> predicate) {
        return spellInstances.stream().anyMatch(predicate);
    }

    public boolean isEmpty() {
        return this == SpellBookInit.EMPTY.get();
    }

    public void runAction(PlayerEntity entity, World world) {
        spellInstances.forEach(spellInstance -> spellInstance.getSpell().getSpellAction().accept(entity, world));
    }
}