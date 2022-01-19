package com.divinity.hlspells.loot;

import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

/**
 * Sets the spell for the given stack
 */
public class SetSpell extends LootFunction {
    private final String spell;

    private SetSpell(ILootCondition[] conditions, String spell) {
        super(conditions);
        this.spell = spell;
    }

    @Override
    protected ItemStack run(ItemStack pStack, LootContext pContext) {
        pStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(spell));
        return pStack;
    }

    @Override
    public LootFunctionType getType() {
        return LootTableHandler.SET_SPELL;
    }

    public static class Serializer extends LootFunction.Serializer<SetSpell> {
        @Override
        public void serialize(JsonObject object, SetSpell setSpell, JsonSerializationContext context) {
            super.serialize(object, setSpell, context);
            object.addProperty("spell", setSpell.spell);
        }

        @Override
        public SetSpell deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, ILootCondition[] pConditions) {
            String spell = JSONUtils.getAsString(pObject, "spell");
            return new SetSpell(pConditions, spell);
        }
    }
}
