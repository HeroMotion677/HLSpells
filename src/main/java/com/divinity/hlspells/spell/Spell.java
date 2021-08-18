package com.divinity.hlspells.spell;

import com.divinity.hlspells.init.SpellInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class Spell extends ForgeRegistryEntry<Spell> {
    private final SpellType spellType;
    private final BiConsumer<PlayerEntity, World> spellAction;
    private final String displayName;
    private int xpCost;
    private int tickDelay;

    @Nullable
    private String descriptionId;

    public Spell(SpellType spellType, BiConsumer<PlayerEntity, World> spellAction, String displayName) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
    }

    public Spell(SpellType spellType, BiConsumer<PlayerEntity, World> spellAction, String displayName, int xpCost) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
    }

    public Spell(SpellType spellType, BiConsumer<PlayerEntity, World> spellAction, String displayName, int xpCost, int tickDelay) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.tickDelay = tickDelay;
    }

    /**
     * Return an spell for the given id
     */
    @Nullable
    public static Spell byId(String id) {
        return SpellInit.SPELLS_REGISTRY.get().getValue(new ResourceLocation(id));
    }

    public boolean isInstantaneous() {
        return false;
    }

    public boolean isCurse() {
        return this.spellType == SpellType.CURSE;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("spell", SpellInit.SPELLS_REGISTRY.get().getKey(this));
        }
        return this.descriptionId;
    }

    public boolean hasCost() {
        return this.xpCost > 0;
    }

    public int getXpCost() {
        return this.xpCost;
    }

    public int getTickDelay() {
        return this.tickDelay;
    }

    public String getTrueDisplayName() {
        return this.displayName;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    public SpellType getType() {
        return this.spellType;
    }

    public BiConsumer<PlayerEntity, World> getSpellAction() {
        return spellAction;
    }
}