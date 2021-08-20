package com.divinity.hlspells.items.capabilities.wandcap;

import com.divinity.hlspells.init.SpellInit;

import java.util.ArrayList;
import java.util.List;

public class SpellHolder implements ISpellHolder {
    private final List<String> spells;
    private int currentSpellCycle;
    private boolean isHeld;

    public SpellHolder() {
        spells = new ArrayList<>();
        currentSpellCycle = 0;
        isHeld = false;
    }

    @Override
    public List<String> getSpells() {
        return this.spells;
    }

    @Override
    public void addSpell(String spell) {
        if (!this.spells.contains(spell)) {
            this.spells.add(spell);
        }
    }

    @Override
    public void removeSpell(String spell) {
        this.spells.remove(spell);
    }

    @Override
    public boolean containsSpell(String spell) {
        return this.spells.contains(spell);
    }

    @Override
    public int getCurrentSpellCycle() {
        return this.currentSpellCycle;
    }

    @Override
    public void setCurrentSpellCycle(int currentSpellCycle) {
        this.currentSpellCycle = currentSpellCycle;
        cycleSpellCheck();
    }

    private void cycleSpellCheck() {
        if (this.currentSpellCycle > this.getSpells().size() - 1) {
            this.currentSpellCycle = 0;
        }
    }

    @Override
    public String getCurrentSpell() {
        return getCurrentSpellCycle() < spells.size() ? spells.get(getCurrentSpellCycle()) : SpellInit.EMPTY.get().getRegistryName().toString();
    }

    @Override
    public boolean isHeldActive() {
        return isHeld;
    }

    @Override
    public void setHeldActive(boolean held) {
        this.isHeld = held;
    }
}
