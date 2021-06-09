package com.heromotion.hlspells.spell;

import net.minecraft.util.text.TextFormatting;

public enum SpellType {

    NORMAL(TextFormatting.GRAY),
    CURSE(TextFormatting.RED);

    private final TextFormatting tooltipFormatting;

    private SpellType(TextFormatting p_i50390_3_) {
        this.tooltipFormatting = p_i50390_3_;
    }

    public TextFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
