package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.ModTotemItem;
import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.StaffItem;
import com.divinity.hlspells.items.WizardArmorMaterial;
import com.divinity.hlspells.items.armor.WizardHatArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HLSpells.MODID);

    public static final RegistryObject<Item> TOTEM_OF_GRIEFING = ITEMS.register("totem_of_griefing", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_KEEPING = ITEMS.register("totem_of_keeping", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_RETURNING = ITEMS.register("totem_of_returning", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_ESCAPING = ITEMS.register("totem_of_escaping", ModTotemItem::new);
    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book", () -> new SpellHoldingItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(300), true));
    public static final RegistryObject<Item> WAND = ITEMS.register("wand", () -> new SpellHoldingItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(700), false));
    public static final RegistryObject<Item> STAFF = ITEMS.register("staff", () -> new StaffItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).durability(1300)));
    public static final RegistryObject<ArmorItem> WIZARD_HAT = ITEMS.register("wizard_hat", () -> new WizardHatArmorItem(WizardArmorMaterial.WIZHAT, EquipmentSlot.HEAD, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
}