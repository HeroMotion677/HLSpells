package com.divinity.hlspells.spell;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.init.SpellInit;
import com.google.common.collect.Maps;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public class Spell extends ForgeRegistryEntry<Spell> {

    private final Map<Attribute, AttributeModifier> attributeModifiers = Maps.newHashMap();
    private final SpellType category;

    @Nullable
    private String descriptionId;

    public Spell(SpellType spellType) {
        this.category = spellType;
    }

    public boolean isInstantenous() {
        return false;
    }

    public boolean isCurse() {
        return this.category == SpellType.CURSE;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("spell", SpellInit.SPELLS_REGISTRY.get().getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    public SpellType getCategory() {
        return this.category;
    }

    @Nullable
    public static Spell byId(String id) {
        return SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(HLSpells.MODID, id));
    }

    public static String getId(Spell spell) {
        return SpellInit.SPELLS_REGISTRY.get().toString();
    }
}