package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.spell.Spell;

import java.util.Arrays;
import java.util.List;

public interface IWandCap
{
    List<String> getSpells();

    void addSpell(String spell);

    void removeSpell(String spell);

    boolean containsSpell(String spell);
}
