package com.divinity.hlspells.loot;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.events.LootEvents;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Sets the spell for the given stack
 */
public class SetSpell extends LootItemConditionalFunction {

    private final String spell;

    private SetSpell(LootItemCondition[] conditions, String spell) {
        super(conditions);
        this.spell = spell;
    }

    @Override
    @NotNull
    protected ItemStack run(ItemStack pStack, @NotNull LootContext pContext) {
        pStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(spell));
        return pStack;
    }

    @Override @NotNull public LootItemFunctionType getType() { return LootEvents.SET_SPELL; }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetSpell> {

        @Override
        @ParametersAreNonnullByDefault
        public void serialize(JsonObject object, SetSpell setSpell, JsonSerializationContext context) {
            super.serialize(object, setSpell, context);
            object.addProperty("spell", setSpell.spell);
        }

        @Override
        @ParametersAreNonnullByDefault
        @NotNull
        public SetSpell deserialize(JsonObject pObject, JsonDeserializationContext pDeserializationContext, LootItemCondition[] pConditions) {
            String spell = GsonHelper.getAsString(pObject, "spell");
            return new SetSpell(pConditions, spell);
        }
    }
}
