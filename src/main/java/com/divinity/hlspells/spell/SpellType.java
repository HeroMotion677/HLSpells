package com.divinity.hlspells.spell;

import net.minecraft.util.text.TextFormatting;

public enum SpellType
{
    CAST(TextFormatting.GRAY),
    HELD(TextFormatting.GOLD),
    CURSE(TextFormatting.RED);

    private final TextFormatting tooltipFormatting;

    SpellType(TextFormatting p_i50390_3_) {
        this.tooltipFormatting = p_i50390_3_;
    }

    public TextFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
