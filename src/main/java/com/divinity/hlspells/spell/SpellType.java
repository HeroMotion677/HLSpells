package com.divinity.hlspells.spell;

import net.minecraft.ChatFormatting;

public enum SpellType {
    CAST(ChatFormatting.GRAY),
    HELD(ChatFormatting.GRAY),
    CURSE(ChatFormatting.RED);

    private final ChatFormatting tooltipFormatting;

    SpellType(ChatFormatting textFormatting) {
        this.tooltipFormatting = textFormatting;
    }

    public ChatFormatting getTooltipFormatting() {
        return this.tooltipFormatting;
    }
}
