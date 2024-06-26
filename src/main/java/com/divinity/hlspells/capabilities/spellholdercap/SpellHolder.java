package com.divinity.hlspells.capabilities.spellholdercap;

import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider.CURRENT_SPELL_CYCLE_NBT;
import static com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider.SPELL_NBT;

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
                this.spells.add(SpellInit.SPELLS_REGISTRY.get().getKey(upgrade).toString());
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
        return getCurrentSpellCycle() < spells.size() ? spells.get(getCurrentSpellCycle()) : Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.get().getKey(SpellInit.EMPTY.get()).toString());
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
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("spellsSize", getSpells().size());
		for (int i = 0; i < getSpells().size(); i++) {
			tag.putString(SPELL_NBT + i, getSpells().get(i));
		}
		tag.putInt(CURRENT_SPELL_CYCLE_NBT, getCurrentSpellCycle());
		return tag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		for (int i = 0; i < nbt.getInt("spellsSize"); i++) {
			addSpell(nbt.getString(SPELL_NBT + i));
		}
		setCurrentSpellCycle(nbt.getInt(CURRENT_SPELL_CYCLE_NBT));
	}
}