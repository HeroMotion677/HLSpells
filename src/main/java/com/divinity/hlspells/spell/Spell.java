package com.divinity.hlspells.spell;

import com.divinity.hlspells.setup.init.SpellInit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.Util;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Spell extends ForgeRegistryEntry<Spell> {
    private final SpellTypes spellTypes;
    private final SpellTypes.SpellRarities spellRarity;
    private SpellTypes.MarkerTypes spellMarkerType;
    private SpellTypes.SpellTiers spellTier;
    private final BiPredicate<Player, Level> spellAction;
    private final String displayName;
    private int xpCost;
    private int tickDelay;
    private boolean treasureOnly;

    @Nullable
    private String descriptionId;

    public Spell(SpellTypes spellTypes, SpellTypes.SpellRarities spellRarity, BiPredicate<Player, Level> spellAction, String displayName) {
        this.spellTypes = spellTypes;
        this.spellRarity = spellRarity;
        this.spellAction = spellAction;
        this.displayName = displayName;
    }

    public Spell(SpellTypes spellTypes, SpellTypes.SpellRarities spellRarity, SpellTypes.MarkerTypes spellMarkerType, SpellTypes.SpellTiers spellTier,
                 BiPredicate<Player, Level> spellAction, String displayName, int xpCost, boolean treasureOnly) {
        this.spellTypes = spellTypes;
        this.spellRarity = spellRarity;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.treasureOnly = treasureOnly;
        this.spellMarkerType = spellMarkerType;
        this.spellTier = spellTier;
    }

    public Spell(SpellTypes spellTypes, SpellTypes.SpellRarities spellRarity, SpellTypes.MarkerTypes spellMarkerType, SpellTypes.SpellTiers spellTier,
                 BiPredicate<Player, Level> spellAction, String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        this.spellTypes = spellTypes;
        this.spellRarity = spellRarity;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.tickDelay = tickDelay;
        this.treasureOnly = treasureOnly;
        this.spellMarkerType = spellMarkerType;
        this.spellTier = spellTier;
    }

    public boolean isCurse() {
        return this.spellTypes == SpellTypes.CURSE;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("spell", SpellInit.SPELLS_REGISTRY.get().getKey(this));
        }
        return this.descriptionId;
    }

    public boolean isTreasureOnly() {
        return this.treasureOnly;
    }

    public SpellTypes.SpellRarities getSpellRarity() {
        return this.spellRarity;
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

    public BaseComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
    }

    public SpellTypes getType() {
        return this.spellTypes;
    }

    public BiPredicate<Player, Level> getSpellAction() {
        return spellAction;
    }

    public boolean test(Predicate<Spell> predicate) {
        return predicate.test(this);
    }

    public boolean isEmpty() {
        return this == SpellInit.EMPTY.get();
    }

    public SpellTypes.MarkerTypes getMarkerType() {
        return this.spellMarkerType;
    }

    public SpellTypes.SpellTiers getSpellTier() {
        return this.spellTier;
    }
}