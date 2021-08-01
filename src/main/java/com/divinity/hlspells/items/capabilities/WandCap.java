package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.spell.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WandCap implements IWandCap
{
    private List<String> spells;

    public WandCap ()
    {
        spells = new ArrayList<>();
    }

    @Override
    public List<String> getSpells()
    {
        return this.spells;
    }

    @Override
    public void addSpell(String spell)
    {
        this.spells.add(spell);
    }

    public void removeSpell(String spell)
    {
        this.spells.remove(spell);
    }

    public boolean containsSpell(String spell)
    {
        return this.spells.contains(spell);
    }
}
