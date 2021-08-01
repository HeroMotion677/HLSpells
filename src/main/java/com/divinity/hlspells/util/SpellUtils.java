package com.divinity.hlspells.util;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellInstance;
import com.divinity.hlspells.init.SpellBookInit;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class SpellUtils
{
    private static final IFormattableTextComponent NO_CONTENT = (new TranslationTextComponent("spell.hlspells.empty")).withStyle(TextFormatting.GRAY);

    public static List<SpellInstance> getSpell(ItemStack stack) {
        return getAllSpells(stack.getTag());
    }

    public static List<SpellInstance> getAllSpells(SpellBookObject spellBookObject, Collection<SpellInstance> spellInstances) {
        List<SpellInstance> list = Lists.newArrayList();
        list.addAll(spellBookObject.getSpells());
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

    public static SpellBookObject getSpellBook(ItemStack stack) {
        return getSpellBook(stack.getTag());
    }

    public static SpellBookObject getSpellBook(@Nullable CompoundNBT nbt) {
        return nbt == null ? SpellBookInit.EMPTY.get() : SpellBookObject.byName(nbt.getString("SpellBook"));
    }

    public static ItemStack setSpellBook(ItemStack stack, SpellBookObject spellBookObject) {
        ResourceLocation resourcelocation = SpellBookInit.SPELL_BOOK_REGISTRY.get().getKey(spellBookObject);
        if (spellBookObject == SpellBookInit.EMPTY.get()) {
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
    public static void addSpellBookTooltip(ItemStack stack, List<ITextComponent> text, float p_185182_2_)
    {
        List<SpellInstance> list = getSpell(stack);
        if (list.isEmpty())
        {
            text.add(NO_CONTENT);
        }
        else
        {
            for (SpellInstance spellInstance : list)
            {
                Spell spell = spellInstance.getSpell();
                text.add(new TranslationTextComponent(spell.getDescriptionId()).withStyle(spell.getCategory().getTooltipFormatting()));
            }
        }
    }
}
