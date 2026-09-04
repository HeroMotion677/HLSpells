package com.divinity.hlspells.loot;

import com.divinity.hlspells.setup.init.ItemInit;
import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EvokerLootModifier extends LootModifier {

    public static final MapCodec<EvokerLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(m -> m.item))
                    .apply(inst, EvokerLootModifier::new));

    private final Item item;

    protected EvokerLootModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() > 0.5) {
            List<ItemStack> toRemove = Lists.newArrayList();
            for (ItemStack stack : generatedLoot) {
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) toRemove.add(stack);
            }
            generatedLoot.removeAll(toRemove);
            generatedLoot.add(new ItemStack(ItemInit.TOTEMS.get(context.getRandom().nextInt(4)).get()));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
