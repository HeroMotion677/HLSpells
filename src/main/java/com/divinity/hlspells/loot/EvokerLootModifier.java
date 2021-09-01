package com.divinity.hlspells.loot;

import com.divinity.hlspells.init.ItemInit;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class EvokerLootModifier extends LootModifier {

    protected EvokerLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        float rand = context.getRandom().nextFloat();
        if (rand > 0.5) {
            List<Item> totems = Lists.newArrayList(ItemInit.TOTEM_OF_ESCAPING.get(), ItemInit.TOTEM_OF_GRIEFING.get(), ItemInit.TOTEM_OF_KEEPING.get(), ItemInit.TOTEM_OF_RETURNING.get());
            generatedLoot.clear();
            generatedLoot.add(new ItemStack(totems.get(context.getRandom().nextInt(4))));
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<EvokerLootModifier> {
        @Override
        public EvokerLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new EvokerLootModifier(conditions);
        }

        @Override
        public JsonObject write(EvokerLootModifier instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
