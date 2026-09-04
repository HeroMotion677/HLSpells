package com.divinity.hlspells.world.structures.villages;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderData;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.setup.init.VillagerInit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@EventBusSubscriber(modid = HLSpells.MODID)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() == VillagerInit.MAGE.get()) {
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(Items.LAPIS_LAZULI),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(Items.AMETHYST_SHARD),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.LAPIS_LAZULI),
                    new ItemStack(Items.EMERALD, 2),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.AMETHYST_SHARD),
                    new ItemStack(Items.EMERALD, 3),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.BOOK),
                    Optional.of(new ItemCost(Items.LAPIS_LAZULI)),
                    enchantedBook(pTrader, Enchantments.FIRE_ASPECT, 2),
                    7,
                    3,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD),
                    new ItemStack(Items.BOOK),
                    7,
                    4,
                    0.05F
            ));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.GLASS_BOTTLE),
                    Optional.of(new ItemCost(Items.LAPIS_LAZULI)),
                    new ItemStack(Items.EXPERIENCE_BOTTLE),
                    6,
                    5,
                    0.05F
            ));
            ItemStack bondSpellBook = new ItemStack(ItemInit.SPELL_BOOK.get());
            SpellHolderProvider.get(bondSpellBook).ifPresent(cap -> cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.getKey(SpellInit.BOND.get())).toString()));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.LAPIS_LAZULI, 10),
                    Optional.of(new ItemCost(ItemInit.SPELL_BOOK.get())),
                    bondSpellBook,
                    6,
                    5,
                    0.05F
            ));
            ItemStack arrowRain = new ItemStack(ItemInit.SPELL_BOOK.get());
            SpellHolderProvider.get(arrowRain).ifPresent(cap -> cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.getKey(SpellInit.ARROW_RAIN.get())).toString()));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    enchantedBookCost(pTrader, Enchantments.POWER, 2),
                    Optional.of(new ItemCost(Items.LAPIS_LAZULI, 5)),
                    arrowRain,
                    6,
                    7,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    enchantedBookCost(pTrader, Enchantments.UNBREAKING, 2),
                    Optional.of(new ItemCost(Items.LAPIS_LAZULI, 10)),
                    new ItemStack(Items.EXPERIENCE_BOTTLE, 4),
                    5,
                    10,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.BOOK),
                    Optional.of(new ItemCost(Items.EMERALD, 30)),
                    new ItemStack(ItemInit.WAND.get()),
                    5,
                    10,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    spellBookCost(bondSpellBook),
                    Optional.of(new ItemCost(Items.LAPIS_LAZULI, 5)),
                    enchantedBook(pTrader, EnchantmentInit.SOUL_BOND, 1),
                    5,
                    15,
                    0.05F
            ));
            trades.get(4).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    Optional.of(new ItemCost(Items.BOOK, 2)),
                    enchantedBook(pTrader, EnchantmentInit.SOUL_SYPHON, 1),
                    4,
                    10,
                    0.05F
            ));
            trades.get(5).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemCost(Items.LAPIS_LAZULI, 10),
                    Optional.of(new ItemCost(Items.EMERALD, 20)),
                    new ItemStack(ItemInit.WIZARD_HAT.get()),
                    3,
                    25,
                    0.05F
            ));
        }
    }

    private static Holder<Enchantment> holder(Entity trader, ResourceKey<Enchantment> key) {
        return trader.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key);
    }

    private static ItemStack enchantedBook(Entity trader, ResourceKey<Enchantment> key, int level) {
        return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder(trader, key), level));
    }

    private static ItemCost enchantedBookCost(Entity trader, ResourceKey<Enchantment> key, int level) {
        ItemStack book = enchantedBook(trader, key, level);
        ItemEnchantments enchantments = book.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        return new ItemCost(Items.ENCHANTED_BOOK).withComponents(builder -> builder.expect(DataComponents.STORED_ENCHANTMENTS, enchantments));
    }

    private static ItemCost spellBookCost(ItemStack spellBook) {
        SpellHolderData data = spellBook.getOrDefault(SpellHolderProvider.SPELL_HOLDER_CAP.get(), SpellHolderData.EMPTY);
        return new ItemCost(ItemInit.SPELL_BOOK.get()).withComponents(builder -> builder.expect(SpellHolderProvider.SPELL_HOLDER_CAP.get(), data));
    }
}
