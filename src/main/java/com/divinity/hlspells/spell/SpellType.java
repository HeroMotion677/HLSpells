package com.divinity.hlspells.spell;

import net.minecraft.util.text.TextFormatting;

public enum SpellType {
    CAST(TextFormatting.GRAY),
    HELD(TextFormatting.GOLD),
    CURSE(TextFormatting.RED);

    private final TextFormatting tooltipFormatting;

    SpellType(TextFormatting textFormatting) {
        this.tooltipFormatting = textFormatting;
    }

    public TextFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
