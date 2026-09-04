package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MenuTypeInit {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, HLSpells.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<AltarOfAttunementMenu>> ALTAR_CONTAINER = MENUS.register("altar_of_attunement",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new AltarOfAttunementMenu(windowId, inv, inv.player, data)));
}
