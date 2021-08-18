package com.divinity.hlspells.spell;

import net.minecraft.nbt.CompoundNBT;

public class SpellInstance {
    private final Spell spell;

    public SpellInstance(Spell spell) {
        this.spell = spell;
    }

    public SpellInstance(SpellInstance spellInstance) {
        this.spell = spellInstance.spell;
    }

    public static SpellInstance load(CompoundNBT nbt) {
        Spell spell = Spell.byId(nbt.getString("Id"));
        return spell == null ? null : loadSpecifiedEffect(spell, nbt);
    }

    private static SpellInstance loadSpecifiedEffect(Spell spell, CompoundNBT nbt) {
        return new SpellInstance(spell);
    }

    public Spell getSpell() {
        return this.spell == null ? null : this.spell.delegate.get();
    }

    public String getDescriptionId() {
        return this.spell.getDescriptionId();
    }

    public String toString() {
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

    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("Id", spell.getRegistryName().toString());
        this.writeDetailsTo(nbt);
        return nbt;
    }

    private void writeDetailsTo(CompoundNBT nbt) {
    }
}
