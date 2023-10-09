package com.divinity.hlspells.capabilities.spellholdercap;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpellHolder implements ISpellHolder {

    private final List<String> spells;
    private int currentSpellCycle;
    private int spellSoundBuffer;

    public SpellHolder() {
        spells = new ArrayList<>();
        currentSpellCycle = 0;
        spellSoundBuffer = 0;
    }

    @Override
    public @NotNull List<String> getSpells() {
        return this.spells;
    }

    @Override
    public void addSpell(String spell) {
        if (this.spells.contains(spell)) {
            Spell spells = SpellUtils.getSpellByID(spell);
            Spell upgrade = spells.getUpgrade();
            if (upgrade != null) {
                this.spells.remove(spell);
                this.spells.add(upgrade.getRegistryName().toString());
            }
        }
        else if (!this.spells.contains(spell)) {
            this.spells.add(spell);
        }
    }

    @Override
    public void removeSpell(String spell) {
        this.spells.remove(spell);
        if (!(this.currentSpellCycle < spells.size())) {
            this.setCurrentSpellCycle(this.getSpells().size() - 1);
        }
    }

    @Override
    public int getCurrentSpellCycle() {
        return this.currentSpellCycle;
    }

    @Override
    public void incrementCurrentSpellCycle() {
        this.setCurrentSpellCycle(this.getCurrentSpellCycle() + 1);
    }

    @Override
    public void setCurrentSpellCycle(int currentSpellCycle) {
        this.currentSpellCycle = currentSpellCycle;
        this.cycleSpellCheck();
    }

    @Override
    @NotNull
    public String getCurrentSpell() {
        return getCurrentSpellCycle() < spells.size() ? spells.get(getCurrentSpellCycle()) : Objects.requireNonNull(SpellInit.EMPTY.get().getRegistryName()).toString();
    }

    @Override
    public int getSpellSoundBuffer() {
        return this.spellSoundBuffer;
    }

    @Override
    public void setSpellSoundBuffer(int spellSoundBuffer) {
        this.spellSoundBuffer = spellSoundBuffer;
    }

    private void cycleSpellCheck() {
        if (this.currentSpellCycle < 0 || this.currentSpellCycle > this.getSpells().size() - 1) {
            this.currentSpellCycle = 0;
        }
    }
}