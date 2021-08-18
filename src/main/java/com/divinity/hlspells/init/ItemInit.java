package com.divinity.hlspells.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.ModTotemItem;
import com.divinity.hlspells.items.SpellBookItem;
import com.divinity.hlspells.items.WandItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            HLSpells.MODID);

    public static final RegistryObject<Item> TOTEM_OF_GRIEFING = ITEMS.register("totem_of_griefing", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_KEEPING = ITEMS.register("totem_of_keeping", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_RETURNING = ITEMS.register("totem_of_returning", ModTotemItem::new);
    public static final RegistryObject<Item> TOTEM_OF_ESCAPING = ITEMS.register("totem_of_escaping", ModTotemItem::new);
    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book", () -> new SpellBookItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS)));
    public static final RegistryObject<Item> WAND = ITEMS.register("wand", () -> new WandItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS)));

}