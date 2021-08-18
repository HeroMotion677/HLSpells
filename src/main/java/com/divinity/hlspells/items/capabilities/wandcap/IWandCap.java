package com.divinity.hlspells.items.capabilities.wandcap;

import java.util.List;

public interface IWandCap {
    List<String> getSpells();

    void addSpell(String spell);

    void removeSpell(String spell);

    boolean containsSpell(String spell);

    int getCurrentSpellCycle();

    void setCurrentSpellCycle(int currentSpellCycle);

    String getCurrentSpell();
}
