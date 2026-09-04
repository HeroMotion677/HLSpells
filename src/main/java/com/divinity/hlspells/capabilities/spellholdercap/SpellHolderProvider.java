package com.divinity.hlspells.capabilities.spellholdercap;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Optional;

public class SpellHolderProvider {

    public static final String CURRENT_SPELL_CYCLE_NBT = "currentSpellCycle";
    public static final String SPELL_NBT = "Spell ";

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, HLSpells.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpellHolderData>> SPELL_HOLDER_CAP =
            COMPONENTS.register("spell_holder", () -> DataComponentType.<SpellHolderData>builder()
                    .persistent(SpellHolderData.CODEC)
                    .networkSynchronized(SpellHolderData.STREAM_CODEC)
                    .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> CAST_BAR =
            COMPONENTS.register("cast_bar", () -> DataComponentType.<Boolean>builder()
                    .persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build());

    public static Optional<ISpellHolder> get(ItemStack stack) {
        return stack.getItem() instanceof SpellHoldingItem ? Optional.of(new SpellHolder(stack)) : Optional.empty();
    }

    @Nullable
    public static ISpellHolder getSpellHolderUnwrap(ItemStack stack) {
        return get(stack).orElse(null);
    }
}
