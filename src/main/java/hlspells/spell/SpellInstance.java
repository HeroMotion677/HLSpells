package com.heromotion.hlspells.spell;

import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpellInstance {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Spell spell;

    public SpellInstance(Spell spell) {
        this.spell = spell;
    }

    public SpellInstance(SpellInstance spellInstance) {
        this.spell = spellInstance.spell;
    }

    public Spell getSpell() {
        return this.spell == null ? null : this.spell.delegate.get();
    }

    public String getDescriptionId() {
        return this.spell.getDescriptionId();
    }

    public String toString()
    {
        String s;
        s = this.getDescriptionId();
        return s;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof SpellInstance)) {
            return false;
        }
        return false;
    }

    public static SpellInstance load(CompoundNBT nbt) {
        Spell spell = Spell.byId(nbt.getString("Id"));
        return spell == null ? null : loadSpecifiedEffect(spell, nbt);
    }

    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("Id", Spell.getId(this.getSpell()));
        this.writeDetailsTo(nbt);
        return nbt;
    }

    private void writeDetailsTo(CompoundNBT nbt) {
        CompoundNBT compoundnbt = new CompoundNBT();
    }

    private static SpellInstance loadSpecifiedEffect(Spell spell, CompoundNBT nbt) {
        return new SpellInstance(spell);
    }
}
