package com.divinity.hlspells.villages;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.EnchantmentInit;
import com.divinity.hlspells.init.ItemInit;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
        if (event.getType() == Villagers.MAGE.get()) {
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.LAPIS_LAZULI),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.LAPIS_LAZULI),
                    new ItemStack(Items.EMERALD, 2),
                    7
                    , 1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.BOOK),
                    new ItemStack(Items.LAPIS_LAZULI),
                    EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.FIRE_ASPECT, 2)),
                    7,
                    3,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD),
                    new ItemStack(Items.BOOK),
                    7,
                    4,
                    0.05F
            ));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.GLASS_BOTTLE),
                    new ItemStack(Items.LAPIS_LAZULI),
                    new ItemStack(Items.EXPERIENCE_BOTTLE),
                    6,
                    5,
                    0.05F
            ));
            ItemStack bondSpellBook = new ItemStack(ItemInit.SPELL_BOOK.get());
            bondSpellBook.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(SpellInit.BOND.get().getRegistryName().toString()));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.LAPIS_LAZULI, 10),
                    new ItemStack(ItemInit.SPELL_BOOK.get()),
                    bondSpellBook,
                    6,
                    5,
                    0.05F
            ));
            ItemStack arrowRain = new ItemStack(ItemInit.SPELL_BOOK.get());
            arrowRain.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(SpellInit.ARROW_RAIN.get().getRegistryName().toString()));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.POWER_ARROWS, 2)),
                    new ItemStack(Items.LAPIS_LAZULI, 5),
                    arrowRain,
                    6,
                    7,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.UNBREAKING, 2)),
                    new ItemStack(Items.LAPIS_LAZULI, 10),
                    new ItemStack(Items.EXPERIENCE_BOTTLE, 4),
                    5,
                    10,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.BOOK),
                    new ItemStack(Items.EMERALD, 30),
                    new ItemStack(ItemInit.WAND.get()),
                    5,
                    10,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    bondSpellBook,
                    new ItemStack(Items.LAPIS_LAZULI, 5),
                    EnchantedBookItem.createForEnchantment(new EnchantmentData(EnchantmentInit.SOUL_BOND.get(), 1)),
                    5,
                    15,
                    0.05F
            ));
            trades.get(4).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 20),
                    new ItemStack(Items.BOOK, 2),
                    EnchantedBookItem.createForEnchantment(new EnchantmentData(EnchantmentInit.SOUL_SYPHON.get(), 1)),
                    4,
                    10,
                    0.05F
            ));
            trades.get(5).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.LAPIS_LAZULI, 10),
                    new ItemStack(Items.EMERALD, 20),
                    new ItemStack(ItemInit.WIZARD_HAT.get()),
                    3,
                    25,
                    0.05F
            ));
        }
    }
}
