package com.divinity.hlspells.items.capabilities.spellholdercap;

import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class SpellHolderProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
    public static final String SPELL_NBT = "Spell ";
    public static Capability<ISpellHolder> SPELL_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private SpellHolder spellHolder = null;
    private final LazyOptional<ISpellHolder> instance = LazyOptional.of(this::createSpellCap);


    @Nonnull
    private ISpellHolder createSpellCap() {
        return spellHolder == null ? new SpellHolder() : spellHolder;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == SPELL_HOLDER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                tag.putInt("spellsSize", cap.getSpells().size());
                if (cap.getSpells() != null) {
                    for (int i = 0; i < cap.getSpells().size(); i++) {
                        tag.putString(SPELL_NBT + i, cap.getSpells().get(i));
                    }
                }
                tag.putInt(CURRENT_SPELL_CYCLE_NBT, cap.getCurrentSpellCycle());
                tag.putBoolean("isHeld", cap.isHeldActive());
            });
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                for (int i = 0; i < nbt.getInt("spellsSize"); i++) {
                    cap.addSpell(nbt.getString(SPELL_NBT + i));
                }
                cap.setCurrentSpellCycle(nbt.getInt(CURRENT_SPELL_CYCLE_NBT));
                cap.setHeldActive(nbt.getBoolean("isHeld"));
            });
        }
    }
}
