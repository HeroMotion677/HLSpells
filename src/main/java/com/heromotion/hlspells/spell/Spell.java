package com.heromotion.hlspells.spell;

import net.minecraft.item.ItemStack;

import java.util.*;

public enum Spell {

    CHANNELING(0, "channeling", 0);

    private final int id;
    private final String name;
    private final int cooldown;

    private static final List<Spell> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    Spell(int p_214976840_0, String p_214976840_1, int p_214976840_2) {
        this.id = p_214976840_0;
        this.name = p_214976840_1;
        this.cooldown = p_214976840_2;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public static Spell randomSpell() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static String randomSpellId() {
        return byId(randomSpell());
    }

    public static Spell byName(String p_214976841_0) {
        return byName(p_214976841_0, CHANNELING);
    }

    public static Spell byName(String p_214976842_0, Spell p_214976842_1) {
        for (Spell spell : values()) {
            if (spell.name.equals(p_214976842_0)) {
                return spell;
            }
        }
        return p_214976842_1;
    }

    public static int byCooldown(ItemStack p_214976843_0) {
        String spell = p_214976843_0.getOrCreateTag().getString("spell");
        return byCooldown(spell);
    }

    public static int byCooldown(String p_214976844_0) {
        return byCooldown(p_214976844_0, 0);
    }

    public static int byCooldown(String p_214976845_0, int p_214976845_1) {
        for (Spell spell : values()) {
            if (spell.name.equals(p_214976845_0)) {
                return spell.cooldown;
            }
        }
        return p_214976845_1;
    }

    public static String byId(Spell p_214976846_0) {
        return byId(p_214976846_0, "channeling");
    }

    public static String byId(Spell p_214976847_0, String p_214976847_1) {
        for (Spell spell : values()) {
            if (spell.equalsTo(p_214976847_0)) {
                return spell.name;
            }
        }
        return p_214976847_1;
    }

    /**
     * Compares this spell to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * Spell} object that represents the same spell as this object.
     *
     * @param anObject The object to compare this {@code Spell} against
     * @return {@code true} if the given object represents a {@code Spell}
     * equivalent to this spell, {@code false} otherwise
     */
    public boolean equalsTo(Object anObject) {
        if (this == anObject) {
            return true;
        }
        return anObject instanceof Spell && ((Spell) anObject).name.equals(this.name);
    }
}
