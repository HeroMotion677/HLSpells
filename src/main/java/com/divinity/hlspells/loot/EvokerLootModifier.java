package com.divinity.hlspells.loot;

import com.divinity.hlspells.setup.init.ItemInit;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class EvokerLootModifier extends LootModifier {

    protected EvokerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        float rand = context.getRandom().nextFloat();
        if (rand > 0.5) {
            List<ItemStack> toRemove = Lists.newArrayList();
            for (ItemStack stack : generatedLoot) {
                if (stack.getItem() == Items.TOTEM_OF_UNDYING) toRemove.add(stack);
            }
            generatedLoot.removeAll(toRemove);
            generatedLoot.add(new ItemStack(ItemInit.TOTEMS.get(context.getRandom().nextInt(4)).get()));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<EvokerLootModifier> {

        @Override
        public EvokerLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
            return new EvokerLootModifier(conditions);
        }

        @Override public JsonObject write(EvokerLootModifier instance) { return this.makeConditions(instance.conditions); }
    }
}
