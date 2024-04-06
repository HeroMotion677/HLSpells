package com.divinity.hlspells.world.blocks.blockentities.inventory;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.items.spellitems.SpellHoldingItem;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.clientbound.SpellCluePacket;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.MenuTypeInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.entity.IllusionerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;


public class AltarOfAttunementMenu extends AbstractContainerMenu implements ContainerListener {

    public AltarOfAttunementBE blockEntity;
    public final int[] costs = new int[3];
    public String[] spellClues = new String[]{"", "", ""};
    public List<List<Spell>> spellsList;
    public List<Spell> topSpellSlot;
    public List<Spell> middleSpellSlot;
    public List<Spell> bottomSpellSlot;

    private final Player playerEntity;
    private final Random random = new Random();
    private final ContainerLevelAccess levelAccess;
    private final DataSlot enchantmentSeed = DataSlot.standalone();

    public AltarOfAttunementMenu(int id, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(id, playerInventory, player, playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    public AltarOfAttunementMenu(int id, Inventory playerInventory, Player player, BlockEntity blockEntity, ContainerLevelAccess levelAccess) {
        super(MenuTypeInit.ALTAR_CONTAINER.get(), id);
        this.levelAccess = levelAccess;
        this.blockEntity = ((AltarOfAttunementBE) blockEntity);
        IItemHandler playerInv = new InvWrapper(playerInventory);
        this.playerEntity = player;
        if (blockEntity != null) {
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
                this.addSlot(new SlotItemHandler(handler, 0, 17, 25) {
                    @Override public boolean mayPlace(@NotNull ItemStack pStack) { return pStack.getItem() instanceof SpellHoldingItem; }
                    @Override public int getMaxStackSize() {
                        return 1;
                    }
                });
                this.addSlot(new SlotItemHandler(handler, 1, 17, 62) {
                    @Override public boolean mayPlace(@NotNull ItemStack pStack) { return pStack.getItem() instanceof SpellHoldingItem; }
                    @Override public int getMaxStackSize() { return 1; }
                });
                this.addSlot(new SlotItemHandler(handler, 2, 143, 62) {
                    @Override public boolean mayPlace(@NotNull ItemStack pStack) { return pStack.getItem() == Items.LAPIS_LAZULI || pStack.getItem() == Items.AMETHYST_SHARD; }
                    @Override public int getMaxStackSize() { return 64; }
                });

                this.addSlot(new SlotItemHandler(handler, 3, 143, 37) {
                    @Override public boolean mayPlace(@NotNull ItemStack pStack) { return pStack.getItem() instanceof SpellHoldingItem; }
                    @Override public int getMaxStackSize() { return 1; }
                });
            });
        }
        this.spellsList = Lists.newArrayList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.topSpellSlot = Lists.newArrayList();
        this.middleSpellSlot = Lists.newArrayList();
        this.bottomSpellSlot = Lists.newArrayList();
        // Add Inventory
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 105 + i * 18));
            }
        }
        // Add Hotbar
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new SlotItemHandler(playerInv, k, 8 + k * 18, 163));
        }
        this.addDataSlot(this.enchantmentSeed).set(player.getEnchantmentSeed());
        this.addDataSlot(DataSlot.shared(this.costs, 0));
        this.addDataSlot(DataSlot.shared(this.costs, 1));
        this.addDataSlot(DataSlot.shared(this.costs, 2));
        this.addSlotListener(this);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return blockEntity.getLevel() != null && stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockInit.ALTAR_OF_ATTUNEMENT_BLOCK.get());
    }

    @Override
    public void slotsChanged(@Nullable Container pInventory) {
        ItemStack materialSlot = blockEntity.itemHandler.getStackInSlot(2);
        ItemStack spellItemSlot = blockEntity.itemHandler.getStackInSlot(3);
        if (spellItemSlot.getItem() instanceof SpellHoldingItem && !materialSlot.isEmpty()) {
            spellItemSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> {
                if (spellHolder.getSpells().isEmpty()) {
                    this.levelAccess.execute((level, pos) -> {
                        for (int i = 0; i < costs.length; ++i) {
                            this.costs[i] = 5 * (i + 1);
                        }
                        SpellAttributes.Marker markerMarker = materialSlot.getItem() == Items.LAPIS_LAZULI ? SpellAttributes.Marker.COMBAT : SpellAttributes.Marker.UTILITY;
                        this.random.setSeed(this.enchantmentSeed.get());
                        this.topSpellSlot = getSpellResultForSlot(this.random, markerMarker, SpellAttributes.Tier.ONE);
                        this.middleSpellSlot = getSpellResultForSlot(this.random, markerMarker, SpellAttributes.Tier.TWO);
                        this.bottomSpellSlot = getSpellResultForSlot(this.random, markerMarker, SpellAttributes.Tier.THREE);
                        this.spellClues[0] = topSpellSlot.get(0).getTrueDisplayName();
                        this.spellClues[1] = middleSpellSlot.get(0).getTrueDisplayName();
                        this.spellClues[2] = bottomSpellSlot.get(0).getTrueDisplayName();
                        this.broadcastChanges();
                    });
                }
                else resetSpellSlots();
            });
        }
        else resetSpellSlots();
        if (this.playerEntity instanceof ServerPlayer player) {
            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SpellCluePacket(player.getUUID(), this.spellClues));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
        if (abstractContainerMenu == this) {
            this.levelAccess.execute((Level level, BlockPos pos) -> {
                if (i <= 3) this.slotsChanged(null);
            });
        }
    }

    @Override public void dataChanged(@NotNull AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {}

    @Override
    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {
        if (pId >= 0 && pId < this.costs.length) {
            ItemStack materialSlot = blockEntity.itemHandler.getStackInSlot(2);
            ItemStack spellItemSlot = blockEntity.itemHandler.getStackInSlot(3);
            int i = pId + 1;
            if ((materialSlot.isEmpty() || materialSlot.getCount() < i) && !pPlayer.getAbilities().instabuild) {
                return false;
            }
            else if (this.costs[pId] <= 0 || spellItemSlot.isEmpty() || (pPlayer.experienceLevel < i || pPlayer.experienceLevel < this.costs[pId]) && !pPlayer.getAbilities().instabuild) {
                return false;
            }
            else {
                this.levelAccess.execute((level, blockPos) -> {
                    Spell spell = pId == 0 ? topSpellSlot.get(0) : pId == 1 ? middleSpellSlot.get(0) : pId == 2 ? bottomSpellSlot.get(0) : null;
                    if (spellItemSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).isPresent() && spell != null) {
                        pPlayer.onEnchantmentPerformed(spellItemSlot, i);
                        spellItemSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> {
                            if (SpellInit.SPELLS_REGISTRY.get().getKey(spell) != null) {
                                spellHolder.addSpell(SpellInit.SPELLS_REGISTRY.get().getKey(spell).toString());
                            }
                        });
                        if (!pPlayer.getAbilities().instabuild) {
                            materialSlot.shrink(i);
                            if (materialSlot.isEmpty()) {
                                this.blockEntity.itemHandler.setStackInSlot(2, ItemStack.EMPTY);
                            }
                        }
                        pPlayer.awardStat(Stats.ENCHANT_ITEM);
                        if (pPlayer instanceof ServerPlayer player) {
                            CriteriaTriggers.ENCHANTED_ITEM.trigger(player, spellItemSlot, i);
                        }
                        this.blockEntity.setChanged();
                        this.enchantmentSeed.set(pPlayer.getEnchantmentSeed());
                        this.slotsChanged(null);
                        level.playSound(null, blockPos, SoundInit.SPELL_ATTUNEMENT.get(), SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = this.getSlot(index);
        //noinspection ConstantConditions
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index > 3 && index <= 39) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, 0, 4, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        }
        else if (index >= 0 && index <= 3) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, 4, 40, true)) {
                return ItemStack.EMPTY;
            }
        }
        else {
            HLSpells.LOGGER.warn("Invalid slotIndex: " + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed.get();
    }

    public int getMaterialCount() {
        ItemStack itemstack = blockEntity.itemHandler.getStackInSlot(2);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    private List<Spell> getSpellResultForSlot(Random pRandom, SpellAttributes.Marker spellMarker, SpellAttributes.Tier spellTier) {
        List<Spell> list = Lists.newArrayList();
        for(Spell spell : SpellInit.SPELLS_REGISTRY.get()) {
            if (!spell.isTreasureOnly() && spell.getSpellRarity() != SpellAttributes.Rarity.NONE) {
                if (spell.getMarkerType() == spellMarker) {
                    if (spell.getSpellTier() == spellTier) {
                        list.add(spell);
                    }
                }
            }
        }
        return Lists.newArrayList(list.get(pRandom.nextInt(list.size())));
/*        int randInt = new Random().nextInt(10);
        if (randInt <= 5) {
            List<Spell> commonSpells = list.stream().filter(p -> p.getSpellRarity() == SpellAttributes.Rarity.COMMON).collect(Collectors.toList());
            return Lists.newArrayList(commonSpells.get(pRandom.nextInt(commonSpells.size())));
        } else if (randInt <= 8) {
            List<Spell> uncommonSpells = list.stream().filter(p -> p.getSpellRarity() == SpellAttributes.Rarity.UNCOMMON).collect(Collectors.toList());
            return Lists.newArrayList(uncommonSpells.get(pRandom.nextInt(uncommonSpells.size())));
        }
        List<Spell> rareSpells = list.stream().filter(p -> p.getSpellRarity() == SpellAttributes.Rarity.RARE).collect(Collectors.toList());
        return Lists.newArrayList(rareSpells.get(pRandom.nextInt(rareSpells.size())));*/
    }

    private void resetSpellSlots() {
        for(int i = 0; i < 3; ++i) {
            this.costs[i] = 0;
            this.spellClues[i] = "";
        }
    }
}
