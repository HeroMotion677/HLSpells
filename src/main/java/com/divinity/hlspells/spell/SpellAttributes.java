package com.divinity.hlspells.spell;

import net.minecraft.ChatFormatting;

public enum SpellAttributes {;

    public enum Type {
        CAST(ChatFormatting.GRAY),
        HELD(ChatFormatting.GRAY);

        private final ChatFormatting tooltipFormatting;

        Type(ChatFormatting textFormatting) {
            this.tooltipFormatting = textFormatting;
        }
        public ChatFormatting getTooltipFormatting() {
            return this.tooltipFormatting;
        }
    }

    public enum Rarity {
        NONE,
        COMMON,
        UNCOMMON,
        RARE
    }

    public enum Tier {
        ONE,
        TWO,
        THREE
    }

    public enum Marker {
        COMBAT,
        UTILITY
    }
}
