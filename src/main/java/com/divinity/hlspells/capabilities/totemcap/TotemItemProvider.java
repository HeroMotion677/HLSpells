package com.divinity.hlspells.capabilities.totemcap;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.ItemInit;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

public class TotemItemProvider {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, HLSpells.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TotemData>> TOTEM_CAP =
            COMPONENTS.register("totem_data", () -> DataComponentType.<TotemData>builder()
                    .persistent(TotemData.CODEC)
                    .networkSynchronized(TotemData.STREAM_CODEC)
                    .build());

    public static Optional<ITotemCap> get(ItemStack stack) {
        return stack.is(ItemInit.TOTEM_OF_RETURNING.get()) || stack.is(ItemInit.TOTEM_OF_KEEPING.get())
                ? Optional.of(new TotemCap(stack)) : Optional.empty();
    }
}
