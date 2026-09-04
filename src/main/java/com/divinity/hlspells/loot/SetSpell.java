package com.divinity.hlspells.loot;

import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.setup.init.LootInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Sets the spell for the given stack
 */
public class SetSpell extends LootItemConditionalFunction {

    public static final MapCodec<SetSpell> CODEC = RecordCodecBuilder.mapCodec(inst ->
            commonFields(inst).and(Codec.STRING.fieldOf("spell").forGetter(function -> function.spell))
                    .apply(inst, SetSpell::new));

    private final String spell;

    private SetSpell(List<LootItemCondition> conditions, String spell) {
        super(conditions);
        this.spell = spell;
    }

    @Override
    @NotNull
    protected ItemStack run(ItemStack pStack, @NotNull LootContext pContext) {
        SpellHolderProvider.get(pStack).ifPresent(cap -> cap.addSpell(spell));
        return pStack;
    }

    @Override
    @NotNull
    public LootItemFunctionType<SetSpell> getType() {
        return LootInit.SET_SPELL.get();
    }
}
