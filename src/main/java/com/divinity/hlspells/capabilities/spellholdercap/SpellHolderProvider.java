package com.divinity.hlspells.capabilities.spellholdercap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class SpellHolderProvider implements ICapabilitySerializable<CompoundTag> {

    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
    public static final String SPELL_NBT = "Spell ";
    public static Capability<ISpellHolder> SPELL_HOLDER_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private SpellHolder spellHolder = null;
    private final LazyOptional<ISpellHolder> instance = LazyOptional.of(this::createSpellCap);

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
            });
        }
    }

    @Nonnull
    private ISpellHolder createSpellCap() {
        return spellHolder == null ? new SpellHolder() : spellHolder;
    }

    @Nullable
    public static ISpellHolder getSpellHolderUnwrap(ItemStack stack) {
        return stack.getCapability(SPELL_HOLDER_CAP).orElse(null);
    }
}
