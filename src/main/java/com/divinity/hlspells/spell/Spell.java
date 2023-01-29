package com.divinity.hlspells.spell;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;

public abstract class Spell extends ForgeRegistryEntry<Spell> implements Cloneable {

    protected final SpellAttributes.Type spellType;
    protected final SpellAttributes.Rarity spellRarity;
    protected final SpellAttributes.Tier spellTier;
    protected final SpellAttributes.Marker spellMarkerType;
    protected String displayName;
    protected final int xpCost;
    protected final boolean treasureOnly;
    protected int spellLevel;
    protected final int maxSpellLevel;
    protected int tickDelay;
    protected boolean canUse;
    protected SoundEvent spellSound;

    @Nullable private String descriptionId;

    public Spell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel) {
        this.spellType = type;
        this.spellRarity = rarity;
        this.spellTier = tier;
        this.spellMarkerType = marker;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.treasureOnly = treasureOnly;
        this.spellLevel = 1;
        this.maxSpellLevel = maxSpellLevel;
        this.canUse = false;
        this.spellSound = SoundEvents.EVOKER_CAST_SPELL;
    }

    public Spell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly, int maxSpellLevel) {
        this(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel);
        this.tickDelay = tickDelay;
    }

    ////////////////////////////////////// GETTERS & SETTERS //////////////////////////////////////
    public Spell setTrueDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }
    public Spell setSpellLevel(int spellLevel) {
        this.spellLevel = spellLevel;
        return this;
    }

    public boolean isEmpty() {
        return this == SpellInit.EMPTY.get();
    }

    public SpellAttributes.Type getSpellType() {
        return this.spellType;
    }
    public SpellAttributes.Rarity getSpellRarity() {
        return this.spellRarity;
    }
    public SpellAttributes.Tier getSpellTier() {
        return this.spellTier;
    }
    public SpellAttributes.Marker getMarkerType() {
        return this.spellMarkerType;
    }

    public int getSpellLevel() {
        return this.spellLevel;
    }
    public int getMaxSpellLevel() {
        return this.maxSpellLevel;
    }
    public int getXpCost() {
        return this.xpCost;
    }
    public boolean isTreasureOnly() {
        return this.treasureOnly;
    }

    public int getTickDelay() {
        return this.tickDelay;
    }

    public String getTrueDisplayName() {
        return this.displayName;
    }
    public BaseComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
    }
    public String getNameForLevel(int level) {
        return this.getRegistryName() + " " + "I".repeat(level);
    }
    public String getDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = net.minecraft.Util.makeDescriptionId("spell", SpellInit.SPELLS_REGISTRY.get().getKey(this));
        }
        return this.descriptionId;
    }

    public SoundEvent getSpellSound() {
        return this.spellSound;
    }
    public final SoundEvent getDefaultSpellSound() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    public int rarityAsInt() {
        return switch (this.getSpellRarity()) {
            case COMMON -> 1;
            case UNCOMMON -> 2;
            case RARE -> 3;
            default -> 0;
        };
    }
    // Meant for spells that have their implementation in other/multiple places
    public boolean canUseSpell() {
        return this.canUse;
    }

    @Nullable public Spell getUpgrade() {
        return null;
    }
    @Nullable public Spell getUpgradeableSpellPath() {
        return null;
    }


    //////////////////////////////////////// FUNCTIONALITY ////////////////////////////////////////
    protected abstract SpellConsumer<Player> getAction();

    public final void execute(Player player, ItemStack stack) {
        if (SpellUtils.checkXpReq(player, this) && this.getAction() != null)
            this.getAction().andThenIfCast(this.onAfterExecute(this, stack)).accept(player);
        else this.canUse = false;
    }

    // This is mainly for spell registration use
    @Override @Nullable public Spell clone() {
        try {
            return (Spell) super.clone();
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Consumer<Player> onAfterExecute(Spell spell, ItemStack stack) {
        return player -> {
            if (!player.isCreative()) {
                if (stack.getDamageValue() >= stack.getMaxDamage() - 1) stack.shrink(1);
                else {
                    switch (spell.getSpellType()) {
                        case CAST:
                            if (!player.level.isClientSide())
                                stack.hurt(calculateSpellHoldingItemDurabilityDamage(stack), player.getRandom(), (ServerPlayer) player);
                            if (HLSpells.CONFIG.spellsUseXP.get())
                                player.giveExperiencePoints(-SpellUtils.getXpReq(player, spell));
                        case HELD:
                            player.getCapability(PlayerCapProvider.PLAYER_CAP).ifPresent(playerCap -> {
                                int durabilityTickCounter = playerCap.getDurabilityTickCounter();
                                int spellXpTickCounter = playerCap.getSpellXpTickCounter();
                                playerCap.setSpellXpTickCounter(spellXpTickCounter + 1);
                                playerCap.setDurabilityTickCounter(durabilityTickCounter + 1);
                                if (spellXpTickCounter == SpellUtils.getTickDelay(player, spell) && HLSpells.CONFIG.spellsUseXP.get()) {
                                    player.giveExperiencePoints(-SpellUtils.getXpReq(player, spell));
                                    playerCap.setSpellXpTickCounter(0);
                                }
                                if (durabilityTickCounter % 15 == 0 && !player.level.isClientSide()) {
                                    stack.hurt(calculateSpellHoldingItemDurabilityDamage(stack), player.getRandom(), (ServerPlayer) player);
                                    playerCap.setDurabilityTickCounter(0);
                                }
                            });
                    }
                }
            }
        };
    }

    private static int calculateSpellHoldingItemDurabilityDamage(ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
        if (level < 1) return 1;
        int random = new Random().nextInt(5);
        return random <= (level - 1) ? 0 : 1;
    }
}