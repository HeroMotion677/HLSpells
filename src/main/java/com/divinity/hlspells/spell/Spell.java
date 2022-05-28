package com.divinity.hlspells.spell;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;

public abstract class Spell extends ForgeRegistryEntry<Spell> {

    private final SpellAttributes.Type spellType;
    private final SpellAttributes.Rarity spellRarity;
    private final SpellAttributes.Marker spellMarkerType;
    private final SpellAttributes.Tier spellTier;
    private final String displayName;
    private final int xpCost;
    private int tickDelay;
    private final boolean treasureOnly;

    @Nullable
    private final String descriptionId;

    public Spell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly) {
        this.spellType = type;
        this.spellRarity = rarity;
        this.spellTier = tier;
        this.spellMarkerType = marker;
        this.displayName = displayName;
        this.xpCost = xpCost;
        this.treasureOnly = treasureOnly;
        this.descriptionId = this.createDescriptionId();
    }

    public Spell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, int tickDelay, boolean treasureOnly) {
        this(type, rarity, tier, marker, displayName, xpCost, treasureOnly);
        this.tickDelay = tickDelay;
    }

    public abstract SpellConsumer<Player> getAction();

    public boolean isTreasureOnly() {
        return this.treasureOnly;
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
        return this.createDescriptionId();
    }

    public BaseComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId());
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

    public int rarityAsInt() {
        return this.getSpellRarity() == SpellAttributes.Rarity.COMMON ? 1 : this.getSpellRarity() == SpellAttributes.Rarity.UNCOMMON ? 2 : this.getSpellRarity() == SpellAttributes.Rarity.RARE ? 3 : 0;
    }

    public final void execute(Player player, ItemStack stack) {
        if (SpellUtils.checkXpReq(player, this) && this.getAction() != null) {
            this.getAction().andThenIfCast(this.onAfterExecute(this, stack)).accept(player);
        }
    }

    protected String createDescriptionId() {
        return this.descriptionId == null ? net.minecraft.Util.makeDescriptionId("spell", SpellInit.SPELLS_REGISTRY.get().getKey(this)) : descriptionId;
    }

    private Consumer<Player> onAfterExecute(Spell spell, ItemStack stack) {
        return player -> {
            if (!player.isCreative()) {
                switch (spell.getSpellType()) {
                    case CAST:
                        if (!player.level.isClientSide())
                            stack.hurt(getSpellHoldingItemCalculation(stack), player.getRandom(), (ServerPlayer) player);
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
                            if (durabilityTickCounter == 15 && !player.level.isClientSide()) {
                                stack.hurt(getSpellHoldingItemCalculation(stack), player.getRandom(), (ServerPlayer) player);
                                playerCap.setDurabilityTickCounter(0);
                            }
                        });
                }
            }
        };
    }

    private static int getSpellHoldingItemCalculation(ItemStack itemStack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, itemStack);
        if (level < 1) return 1;
        int random = new Random().nextInt(5);
        return random <= (level - 1) ? 0 : 1;
    }
}