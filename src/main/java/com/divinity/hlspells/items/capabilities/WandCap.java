package com.divinity.hlspells.items.capabilities;

import com.divinity.hlspells.spell.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WandCap implements IWandCap
{
    private List<String> spells;
    private int currentSpellCycle;

    public WandCap ()
    {
        spells = new ArrayList<>();
        currentSpellCycle = 0;
    }

    @Override
    public List<String> getSpells()
    {
        return this.spells;
    }

    @Override
    public void addSpell(String spell)
    {
        if (!this.spells.contains(spell) && this.spells.size() < 3)
        {
            this.spells.add(spell);
        }
    }

    @Override
    public void removeSpell(String spell)
    {
        this.spells.remove(spell);
    }

    @Override
    public boolean containsSpell(String spell)
    {
        return this.spells.contains(spell);
    }

    @Override
    public int getCurrentSpellCycle()
    {
        spellCycleCheck();
        return this.currentSpellCycle;
    }

    @Override
    public void setCurrentSpellCycle(int currentSpellCycle)
    {
        this.currentSpellCycle = currentSpellCycle;
        spellCycleCheck();
    }

    private void spellCycleCheck()
    {
        if (this.currentSpellCycle >= 3)
        {
            this.currentSpellCycle = 0;
        }
    }
}
