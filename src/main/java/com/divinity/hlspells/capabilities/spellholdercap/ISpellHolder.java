package com.divinity.hlspells.capabilities.spellholdercap;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ISpellHolder {

    @NotNull List<String> getSpells();

    /**
     * Do your own check to see if the spell can be added
     */
    void addSpell(String spell);

    void removeSpell(String spell);

    int getCurrentSpellCycle();

    void incrementCurrentSpellCycle();

    void setCurrentSpellCycle(int currentSpellCycle);

    @NotNull String getCurrentSpell();

    int getSpellSoundBuffer();

    void setSpellSoundBuffer(int spellSoundBuffer);
}
