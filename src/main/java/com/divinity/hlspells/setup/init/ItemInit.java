package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.items.spellitems.StaffItem;
import com.divinity.hlspells.items.armor.material.WizardArmorMaterial;
import com.divinity.hlspells.items.armor.WizardHatArmorItem;
import com.divinity.hlspells.items.totems.EscapingTotem;
import com.divinity.hlspells.items.totems.GriefingTotem;
import com.divinity.hlspells.items.totems.KeepingTotem;
import com.divinity.hlspells.items.totems.ReturningTotem;
import com.divinity.hlspells.spell.Spell;
import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Objects;

import static net.minecraft.world.item.Rarity.EPIC;
import static net.minecraft.world.item.Rarity.RARE;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HLSpells.MODID);

    public static final RegistryObject<Item> GEM_UPGRADE_TEMPLATE = ITEMS.register("gem_upgrade_template", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_UPGRADE_TEMPLATE = ITEMS.register("gold_upgrade_template", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TOTEM_OF_ESCAPING = ITEMS.register("totem_of_escaping", EscapingTotem::new);
    public static final RegistryObject<Item> TOTEM_OF_RETURNING = ITEMS.register("totem_of_returning", ReturningTotem::new);
    public static final RegistryObject<Item> TOTEM_OF_GRIEFING = ITEMS.register("totem_of_griefing", GriefingTotem::new);
    public static final RegistryObject<Item> TOTEM_OF_KEEPING = ITEMS.register("totem_of_keeping", KeepingTotem::new);
    public static final RegistryObject<Item> SPELL_BOOK = ITEMS.register("spell_book", () -> new SpellHoldingItem(new Item.Properties().durability(100), true));
    public static final RegistryObject<Item> WAND = ITEMS.register("lapis_wand", () -> new SpellHoldingItem(new Item.Properties().durability(150), false));
    public static final RegistryObject<Item> AMETHYST_WAND = ITEMS.register("amethyst_wand", () -> new SpellHoldingItem(new Item.Properties().durability(150), false));
    public static final RegistryObject<Item> WIZARD_HAT = ITEMS.register("wizard_hat", () -> new WizardHatArmorItem(WizardArmorMaterial.WIZHAT, ArmorItem.Type.HELMET, new Item.Properties().rarity(RARE)));
    public static final RegistryObject<BlockItem> ALTAR_ITEM = ITEMS.register("altar_of_attunement", () -> new BlockItem(BlockInit.ALTAR_OF_ATTUNEMENT_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> ORB_ITEM = ITEMS.register("orb_of_enchanting", () -> new BlockItem(BlockInit.ORB_OF_ENCHANTING.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> TENT_ITEM = ITEMS.register("tent", () -> new BlockItem(BlockInit.TENT.get(), new Item.Properties()));

    // Staffs
    public static final RegistryObject<Item> WOODEN_STAFF = ITEMS.register("wooden_lapis_staff", () ->
            new StaffItem(new Item.Properties().rarity(RARE).durability(200),
                    2D, -3.2D, true,0.26 * 20, false, false, 50, () ->  Ingredient.of(ItemTags.PLANKS)));

    public static final RegistryObject<Item> GOLDEN_STAFF = ITEMS.register("golden_lapis_staff", () ->
            new StaffItem(new Item.Properties().rarity(EPIC).durability(1500),
                    5D, -2.5D, true,0.41 * 20, false, false, 30, () ->  Ingredient.of(new ItemLike[]{Items.GOLD_INGOT})));

    public static final RegistryObject<Item> NETHER_STAFF = ITEMS.register("netherite_lapis_staff", () ->
            new StaffItem(new Item.Properties().rarity(EPIC).durability(2000),
                    7D, -3D, true,0.31 * 20, false, true, 44, () ->  Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT})));

    // Amethyst Variants
    public static final RegistryObject<Item> WOODEN_STAFF_AMETHYST = ITEMS.register("wooden_amethyst_staff", () ->
            new StaffItem(new Item.Properties().rarity(RARE).durability(200),
                    2D, -3.2D, true,0.40 * 20, true, false, 50, () ->  Ingredient.of(ItemTags.PLANKS)));

    public static final RegistryObject<Item> GOLDEN_STAFF_AMETHYST = ITEMS.register("golden_amethyst_staff", () ->
            new StaffItem(new Item.Properties().rarity(EPIC).durability(1500),
                    5D, -2.5D, true, 0.60 * 20, true, false, 25, () ->  Ingredient.of(new ItemLike[]{Items.GOLD_INGOT})));

    public static final RegistryObject<Item> NETHER_STAFF_AMETHYST = ITEMS.register("netherite_amethyst_staff", () ->
            new StaffItem(new Item.Properties().rarity(EPIC).durability(2000),
                    7D, -3D,  true, 0.45 * 20, true, true, 40, () ->  Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT})));


    public static final List<RegistryObject<Item>> STAFFS = Lists.newArrayList(WOODEN_STAFF, WOODEN_STAFF_AMETHYST, GOLDEN_STAFF, GOLDEN_STAFF_AMETHYST, NETHER_STAFF, NETHER_STAFF_AMETHYST);

    // Totems should be ordered from: Activates Before Death -> Cancels Death -> Requires Death
    public static final List<RegistryObject<Item>> TOTEMS = Lists.newArrayList(TOTEM_OF_GRIEFING,
                                                                               TOTEM_OF_ESCAPING,
                                                                               TOTEM_OF_RETURNING,
                                                                               TOTEM_OF_KEEPING);

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.COMBAT){
            event.accept(WIZARD_HAT);

            event.accept(WOODEN_STAFF);
            event.accept(WOODEN_STAFF_AMETHYST);

            event.accept(GOLDEN_STAFF);
            event.accept(GOLDEN_STAFF_AMETHYST);

            event.accept(NETHER_STAFF);
            event.accept(NETHER_STAFF_AMETHYST);

            event.accept(TOTEM_OF_ESCAPING);
            event.accept(TOTEM_OF_GRIEFING);
            event.accept(TOTEM_OF_KEEPING);
            event.accept(TOTEM_OF_RETURNING);
        }
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(WAND);
            event.accept(AMETHYST_WAND);

            event.accept(GEM_UPGRADE_TEMPLATE);
            event.accept(GOLD_UPGRADE_TEMPLATE);

            event.accept(SPELL_BOOK);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(GEM_UPGRADE_TEMPLATE);
            event.accept(GOLD_UPGRADE_TEMPLATE);

        }
        if(event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(ORB_ITEM);
            event.accept(ALTAR_ITEM);
            event.accept(TENT_ITEM);
        }
    }


 }