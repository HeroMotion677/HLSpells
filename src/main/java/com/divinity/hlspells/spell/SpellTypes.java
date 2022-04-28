package com.divinity.hlspells.spell;

import net.minecraft.ChatFormatting;

public enum SpellTypes {
    CAST(ChatFormatting.GRAY),
    HELD(ChatFormatting.GRAY),
    CURSE(ChatFormatting.RED);

    public enum SpellRarities {
        NONE,
        COMMON,
        UNCOMMON,
        RARE
    }

    public enum SpellTiers {
        TIER_ONE,
        TIER_TWO,
        TIER_THREE
    }

    public enum MarkerTypes {
        COMBAT,
        UTILITY
    }

    private final ChatFormatting tooltipFormatting;

    SpellTypes(ChatFormatting textFormatting) {
        this.tooltipFormatting = textFormatting;
    }

    public ChatFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
