package com.heromotion.hlspells.util;

import com.google.common.collect.Lists;

import com.heromotion.hlspells.init.SpellBookInit;
import com.heromotion.hlspells.spell.*;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;

import net.minecraftforge.api.distmarker.*;

import java.util.*;
import javax.annotation.Nullable;

public class SpellUtils {

    private static final IFormattableTextComponent NO_CONTENT = (new TranslationTextComponent("spell.hlspells.empty")).withStyle(TextFormatting.GRAY);

    public static List<SpellInstance> getSpell(ItemStack stack) {
        return getAllSpells(stack.getTag());
    }

    public static List<SpellInstance> getAllSpells(SpellBook spellBook, Collection<SpellInstance> spellInstances) {
        List<SpellInstance> list = Lists.newArrayList();
        list.addAll(spellBook.getSpells());
        list.addAll(spellInstances);
        return list;
    }

    public static List<SpellInstance> getAllSpells(@Nullable CompoundNBT nbt) {
        List<SpellInstance> list = Lists.newArrayList();
        list.addAll(getSpellBook(nbt).getSpells());
        getCustomSpells(nbt, list);
        return list;
    }

    public static List<SpellInstance> getCustomSpells(ItemStack stack) {
        return getCustomSpells(stack.getTag());
    }

    public static List<SpellInstance> getCustomSpells(@Nullable CompoundNBT nbt) {
        List<SpellInstance> list = Lists.newArrayList();
        getCustomSpells(nbt, list);
        return list;
    }

    public static void getCustomSpells(@Nullable CompoundNBT nbt, List<SpellInstance> spellInstances) {
        if (nbt != null && nbt.contains("CustomSpellEffects", 9)) {
            ListNBT listNBT = nbt.getList("CustomSpellEffects", 10);

            for (int i = 0; i < listNBT.size(); i++) {
                CompoundNBT compoundNBT = listNBT.getCompound(i);
                SpellInstance spellInstance = SpellInstance.load(compoundNBT);
                if (spellInstance != null) {
                    spellInstances.add(spellInstance);
                }
            }
        }
    }

    public static SpellBook getSpellBook(ItemStack stack) {
        return getSpellBook(stack.getTag());
    }

    public static SpellBook getSpellBook(@Nullable CompoundNBT nbt) {
        return nbt == null ? SpellBookInit.EMPTY.get() : SpellBook.byName(nbt.getString("SpellBook"));
    }

    public static ItemStack setSpellBook(ItemStack stack, SpellBook spellBook) {
        ResourceLocation resourcelocation = SpellBookInit.SPELL_BOOK_REGISTRY.get().getKey(spellBook);
        if (spellBook == SpellBookInit.EMPTY.get()) {
            stack.removeTagKey("SpellBook");
        } else {
            stack.getOrCreateTag().putString("SpellBook", resourcelocation.toString());
        }

        return stack;
    }

    public static ItemStack setCustomSpells(ItemStack stack, Collection<SpellInstance> spellInstances) {
        if (!spellInstances.isEmpty()) {
            CompoundNBT compoundnbt = stack.getOrCreateTag();
            ListNBT listnbt = compoundnbt.getList("CustomSpellEffects", 9);

            for (SpellInstance spellInstance : spellInstances) {
                listnbt.add(spellInstance.save(new CompoundNBT()));
            }

            compoundnbt.put("CustomSpellEffects", listnbt);
        }
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public static void addSpellBookTooltip(ItemStack stack, List<ITextComponent> text, float p_185182_2_) {
        List<SpellInstance> list = getSpell(stack);
        if (list.isEmpty()) {
            text.add(NO_CONTENT);
        } else {
            for (SpellInstance spellInstance : list) {
                Spell spell = spellInstance.getSpell();
                text.add(new TranslationTextComponent(spell.getDescriptionId()).withStyle(spell.getCategory().getTooltipFormatting()));
            }
        }
    }
}
