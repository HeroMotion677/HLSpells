package com.divinity.hlspells.spell;

import com.divinity.hlspells.init.SpellInit;
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
    private final SpellType spellType;
    private final BiPredicate<Player, Level> spellAction;
    private final String displayName;
    private int xpCost;
    private int tickDelay;

    @Nullable
    private String descriptionId;

    public Spell(SpellType spellType, BiPredicate<Player, Level> spellAction, String displayName) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
    }

    public Spell(SpellType spellType, BiPredicate<Player, Level> spellAction, String displayName, int xpCost) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
    }

    public Spell(SpellType spellType, BiPredicate<Player, Level> spellAction, String displayName, int xpCost, int tickDelay) {
        this.spellType = spellType;
        this.spellAction = spellAction;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.tickDelay = tickDelay;
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

    public BaseComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
    }

    public SpellType getType() {
        return this.spellType;
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
}