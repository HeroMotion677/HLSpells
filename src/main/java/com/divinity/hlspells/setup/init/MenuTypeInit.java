package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuTypeInit {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, HLSpells.MODID);

    public static final RegistryObject<MenuType<AltarOfAttunementMenu>> ALTAR_CONTAINER = MENUS.register("altar_of_attunement",
            () -> IForgeMenuType.create((windowId, inv, data) -> new AltarOfAttunementMenu(windowId, inv, inv.player, data)));
}
