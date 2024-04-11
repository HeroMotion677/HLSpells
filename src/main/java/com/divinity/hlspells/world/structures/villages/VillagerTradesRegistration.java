package com.divinity.hlspells.world.structures.villages;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.setup.init.VillagerInit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = HLSpells.MODID)
public class VillagerTradesRegistration {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (event.getType() == VillagerInit.MAGE.get()) {
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.LAPIS_LAZULI),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 3),
                    new ItemStack(Items.AMETHYST_SHARD),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.LAPIS_LAZULI),
                    new ItemStack(Items.EMERALD, 2),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.AMETHYST_SHARD),
                    new ItemStack(Items.EMERALD, 3),
                    7,
                    1,
                    0.05F
            ));
            trades.get(1).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.BOOK),
                    new ItemStack(Items.LAPIS_LAZULI),
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.FIRE_ASPECT, 2)),
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
            bondSpellBook.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.get().getKey(SpellInit.BOND.get()).toString())));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.LAPIS_LAZULI, 10),
                    new ItemStack(ItemInit.SPELL_BOOK.get()),
                    bondSpellBook,
                    6,
                    5,
                    0.05F
            ));
            ItemStack arrowRain = new ItemStack(ItemInit.SPELL_BOOK.get());
            arrowRain.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.get().getKey(SpellInit.ARROW_RAIN.get())).toString()));
            trades.get(2).add((pTrader, pRand) -> new MerchantOffer(
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.POWER_ARROWS, 2)),
                    new ItemStack(Items.LAPIS_LAZULI, 5),
                    arrowRain,
                    6,
                    7,
                    0.05F
            ));
            trades.get(3).add((pTrader, pRand) -> new MerchantOffer(
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.UNBREAKING, 2)),
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
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(EnchantmentInit.SOUL_BOND.get(), 1)),
                    5,
                    15,
                    0.05F
            ));
            trades.get(4).add((pTrader, pRand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 20),
                    new ItemStack(Items.BOOK, 2),
                    EnchantedBookItem.createForEnchantment(new EnchantmentInstance(EnchantmentInit.SOUL_SYPHON.get(), 1)),
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
