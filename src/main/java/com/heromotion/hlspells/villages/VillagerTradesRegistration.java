package com.heromotion.hlspells.villages;

import com.heromotion.hlspells.HLSpells;
import com.heromotion.hlspells.init.EnchantmentInit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = HLSpells.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
        if (event.getType() == Villagers.MAGE.get()) {
            /**
             * Price: 3 Emeralds
             * Price2: None
             * Sale: 2 Lapis Lazuli
             * Max trades: 5
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(1).add(new RandomTradeBuilder(5, 3, 0.05F)
                    .setPrice(Items.EMERALD, 3, 3)
                    .setForSale(Items.LAPIS_LAZULI, 2, 2)
                    .build()
            );
            /**
             * Price: 1 Lapis Lazuli
             * Price2: None
             * Sale: 2 Emeralds
             * Max trades: 5
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(1).add(new RandomTradeBuilder(5, 3, 0.05F)
                    .setPrice(Items.LAPIS_LAZULI, 1, 1)
                    .setForSale(Items.EMERALD, 2, 2)
                    .build()
            );
            /**
             * Price: 1 Book
             * Price2: 1 Lapis Lazuli
             * Sale: 1 Fire Aspect Book
             * Max trades: 5
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(1).add(new RandomTradeBuilder(5, 3, 0.05F)
                    .setPrice(Items.BOOK, 1, 1)
                    .setPrice2(Items.LAPIS_LAZULI, 1, 1)
                    .setForSale(EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.FIRE_ASPECT, 0)).getItem(), 1, 1)
                    .build()
            );
            /**
             * Price: 1 Emerald
             * Price2: None
             * Sale: 1 Book
             * Max trades: 5
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(1).add(new RandomTradeBuilder(5, 3, 0.05F)
                    .setPrice(Items.EMERALD, 1, 1)
                    .setForSale(Items.BOOK, 1, 1)
                    .build()
            );
            /**
             * Price: 1 Empty Bottle
             * Price2: 1 Lapis Lazuli
             * Sale: 1 XP Bottle
             * Max trades: 4
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(2).add(new RandomTradeBuilder(4, 3, 0.05F)
                    .setPrice(Items.GLASS_BOTTLE, 1, 1)
                    .setPrice2(Items.LAPIS_LAZULI, 1, 1)
                    .setForSale(Items.EXPERIENCE_BOTTLE, 1, 1)
                    .build()
            );
            /**
             * Price: 10 Lapis Lazuli
             * Price2: 1 Book
             * Sale: 1 Soul Bond Book
             * Max trades: 4
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(2).add(new RandomTradeBuilder(4, 3, 0.05F)
                    .setPrice(Items.LAPIS_LAZULI, 10, 10)
                    .setPrice2(Items.BOOK, 1, 1)
                    .setForSale(EnchantedBookItem.createForEnchantment(new EnchantmentData(EnchantmentInit.SOUL_BOND.get(), 0)).getItem(), 1, 1)
                    .build()
            );
            /**
             * Price: 1 Power Book
             * Price2: 5 Lapis Lazuli
             * Sale: 1 Infinity Book
             * Max trades: 4
             * XP: 3
             * Price multiplier: 0.05F
             */
            trades.get(2).add(new RandomTradeBuilder(4, 3, 0.05F)
                    .setPrice(EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.POWER_ARROWS, 0)).getItem(), 1, 1)
                    .setPrice2(Items.LAPIS_LAZULI, 5, 5)
                    .setForSale(EnchantedBookItem.createForEnchantment(new EnchantmentData(Enchantments.INFINITY_ARROWS, 0)).getItem(), 1, 1)
                    .build()
            );
            /**
             * Price: 20 Emeralds
             * Price2: None
             * Sale: 1 Conveyor
             * Max trades: 10
             * XP: 5
             * Price multiplier: 0.05F
             */
            /*
            trades.get(3).add(new RandomTradeBuilder(10, 5, 0.05F)
                    .setPrice(Items.EMERALD, 20, 20)
                    .setForSale(BlockInit.CONVEYOR.get().asItem(), 1, 1)
                    .build()
            );
            /**
             * Price: 25 Emeralds
             * Price2: None
             * Sale: 1 Mining Machine
             * Max trades: 7
             * XP: 5
             * Price multiplier: 0.05F
             */
            /*
            trades.get(4).add(new RandomTradeBuilder(7, 5, 0.05F)
                    .setPrice(Items.EMERALD, 25, 25)
                    .setForSale(BlockInit.MINING_MACHINE.get().asItem(), 1, 1)
                    .build()
            );
            /**
             * Price: 30 Emeralds
             * Price2: None
             * Sale: 1 Mechanic Wing
             * Max trades: 5
             * XP: 5
             * Price multiplier: 0.05F
             */
            /*
            trades.get(5).add(new RandomTradeBuilder(5, 5, 0.05F)
                    .setPrice(Items.EMERALD, 30, 30)
                    .setForSale(ItemInit.MECHANIC_WINGS.get(), 1, 1)
                    .build()
            );
             */
        }
    }
}
