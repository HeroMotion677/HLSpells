package com.divinity.hlspells.capabilities.spellholdercap;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellHolder implements ISpellHolder {

    private final ItemStack stack;

    public SpellHolder(ItemStack stack) {
        this.stack = stack;
    }

    private SpellHolderData data() {
        return stack.getOrDefault(SpellHolderProvider.SPELL_HOLDER_CAP.get(), SpellHolderData.EMPTY);
    }

    private void setSpells(List<String> spells) {
        SpellHolderData data = data();
        stack.set(SpellHolderProvider.SPELL_HOLDER_CAP.get(), new SpellHolderData(List.copyOf(spells), data.currentSpellCycle(), data.spellSoundBuffer()));
    }

    @Override
    public @NotNull List<String> getSpells() {
        return data().spells();
    }

    @Override
    public void addSpell(String spell) {
        List<String> spells = new ArrayList<>(getSpells());
        if (spells.contains(spell)) {
            Spell existing = SpellUtils.getSpellByID(spell);
            Spell upgrade = existing.getUpgrade();
            if (upgrade != null) {
                spells.remove(spell);
                spells.add(SpellInit.SPELLS_REGISTRY.getKey(upgrade).toString());
                setSpells(spells);
            }
        }
        else {
            spells.add(spell);
            setSpells(spells);
        }
    }

    @Override
    public void removeSpell(String spell) {
        List<String> spells = new ArrayList<>(getSpells());
        spells.remove(spell);
        setSpells(spells);
        if (!(getCurrentSpellCycle() < spells.size())) {
            this.setCurrentSpellCycle(spells.size() - 1);
        }
    }

    @Override
    public int getCurrentSpellCycle() {
        return data().currentSpellCycle();
    }

    @Override
    public void incrementCurrentSpellCycle() {
        this.setCurrentSpellCycle(this.getCurrentSpellCycle() + 1);
    }

    @Override
    public void setCurrentSpellCycle(int currentSpellCycle) {
        if (currentSpellCycle < 0 || currentSpellCycle > this.getSpells().size() - 1) {
            currentSpellCycle = 0;
        }
        SpellHolderData data = data();
        stack.set(SpellHolderProvider.SPELL_HOLDER_CAP.get(), new SpellHolderData(data.spells(), currentSpellCycle, data.spellSoundBuffer()));
    }

    @Override
    @NotNull
    public String getCurrentSpell() {
        List<String> spells = getSpells();
        return getCurrentSpellCycle() < spells.size() ? spells.get(getCurrentSpellCycle())
                : Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.getKey(SpellInit.EMPTY.get())).toString();
    }

    @Override
    public int getSpellSoundBuffer() {
        return data().spellSoundBuffer();
    }

    @Override
    public void setSpellSoundBuffer(int spellSoundBuffer) {
        SpellHolderData data = data();
        stack.set(SpellHolderProvider.SPELL_HOLDER_CAP.get(), new SpellHolderData(data.spells(), data.currentSpellCycle(), spellSoundBuffer));
    }
}
