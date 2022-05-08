package com.divinity.hlspells.setup.client.inventory;

import com.divinity.hlspells.items.SpellHoldingItem;
import com.divinity.hlspells.items.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.network.NetworkManager;
import com.divinity.hlspells.network.packets.SpellCluePacket;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.MenuTypeInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellTypes;
import com.divinity.hlspells.world.blocks.blockentities.AltarOfAttunementBE;
import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class AltarOfAttunementMenu extends AbstractContainerMenu implements ContainerListener {
    private final Player playerEntity;
    private final Random random = new Random();
    private final ContainerLevelAccess levelAccess;
    private final DataSlot enchantmentSeed = DataSlot.standalone();
    public AltarOfAttunementBE blockEntity;
    public final int[] costs = new int[3];
    public String[] spellClues = new String[]{"", "", ""};
    public List<Spell> topSpellSlot;
    public List<Spell> middleSpellSlot;
    public List<Spell> bottomSpellSlot;

    public AltarOfAttunementMenu(int id, Inventory playerInventory, Player player, FriendlyByteBuf extraData) {
        this(id, playerInventory, player, playerInventory.player.level.getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    public AltarOfAttunementMenu(int id, Inventory playerInventory, Player player, BlockEntity blockEntity, ContainerLevelAccess levelAccess) {
        super(MenuTypeInit.ALTAR_CONTAINER.get(), id);
        checkContainerSize(playerInventory, 4);
        this.levelAccess = levelAccess;
        this.blockEntity = ((AltarOfAttunementBE) blockEntity);
        IItemHandler playerInv = new InvWrapper(playerInventory);
        if (blockEntity != null) {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                this.addSlot(new SlotItemHandler(h, 0, 17, 25) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.getItem() instanceof SpellHoldingItem;
                    }
                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                });
                this.addSlot(new SlotItemHandler(h, 1, 17, 62) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.getItem() instanceof SpellHoldingItem;
                    }

                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                });

                // Attunement slots
                this.addSlot(new SlotItemHandler(h, 2, 143, 62) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.getItem() == Items.LAPIS_LAZULI || pStack.getItem() == Items.AMETHYST_SHARD;
                    }

                    @Override
                    public int getMaxStackSize() {
                        return 64;
                    }
                });

                this.addSlot(new SlotItemHandler(h, 3, 143, 37) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return pStack.getItem() instanceof SpellHoldingItem;
                    }

                    @Override
                    public int getMaxStackSize() {
                        return 1;
                    }
                });
            });
        }
        this.topSpellSlot = Lists.newArrayList();
        this.middleSpellSlot = Lists.newArrayList();
        this.bottomSpellSlot = Lists.newArrayList();
        this.playerEntity = player;

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
    public boolean stillValid(Player pPlayer) {
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
                        SpellTypes.MarkerTypes markerType = materialSlot.getItem() == Items.LAPIS_LAZULI ? SpellTypes.MarkerTypes.COMBAT : SpellTypes.MarkerTypes.UTILITY;
                        this.random.setSeed(this.enchantmentSeed.get());
                        this.topSpellSlot = getSpellResultForSlot(this.random, markerType, SpellTypes.SpellTiers.TIER_ONE);
                        this.middleSpellSlot = getSpellResultForSlot(this.random, markerType, SpellTypes.SpellTiers.TIER_TWO);
                        this.bottomSpellSlot = getSpellResultForSlot(this.random, markerType, SpellTypes.SpellTiers.TIER_THREE);
                        this.spellClues[0] = topSpellSlot.get(0).getTrueDisplayName();
                        this.spellClues[1] = middleSpellSlot.get(0).getTrueDisplayName();
                        this.spellClues[2] = bottomSpellSlot.get(0).getTrueDisplayName();
                        this.broadcastChanges();
                    });
                } else {
                    resetSpellSlots();
                }
            });
        } else {
            resetSpellSlots();
        }
        if (this.playerEntity instanceof ServerPlayer player) {
            NetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SpellCluePacket(player.getUUID(), this.spellClues));
        }
    }

    private void resetSpellSlots() {
        for(int i = 0; i < 3; ++i) {
            this.costs[i] = 0;
        }
        for (int x = 0; x < 3; ++x) {
            this.spellClues[x] = "";
        }
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack) {
        if (abstractContainerMenu == this) {
            this.levelAccess.execute((Level level, BlockPos pos) -> {
                if (i <= 3) {
                    this.slotsChanged(null);
                }
            });
        }
    }

    @Override
    public void dataChanged(AbstractContainerMenu pContainerMenu, int pDataSlotIndex, int pValue) {}

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (pId >= 0 && pId < this.costs.length) {
            ItemStack materialSlot = blockEntity.itemHandler.getStackInSlot(2);
            ItemStack spellItemSlot = blockEntity.itemHandler.getStackInSlot(3);
            int i = pId + 1;
            if ((materialSlot.isEmpty() || materialSlot.getCount() < i) && !pPlayer.getAbilities().instabuild) {
                return false;
            } else if (this.costs[pId] <= 0 || spellItemSlot.isEmpty() || (pPlayer.experienceLevel < i || pPlayer.experienceLevel < this.costs[pId]) && !pPlayer.getAbilities().instabuild) {
                return false;
            } else {
                this.levelAccess.execute((level, blockPos) -> {
                    Spell singular = pId == 0 ? topSpellSlot.get(0) : pId == 1 ? middleSpellSlot.get(0) : pId == 2 ? bottomSpellSlot.get(0) : null;
                    if (spellItemSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).isPresent() && (singular != null)) {
                        pPlayer.onEnchantmentPerformed(spellItemSlot, i);
                        spellItemSlot.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> {
                            if (singular.getRegistryName() != null) {
                                spellHolder.addSpell(singular.getRegistryName().toString());
                            }
                        });
                        if (!pPlayer.getAbilities().instabuild) {
                            materialSlot.shrink(i);
                            if (materialSlot.isEmpty()) {
                                blockEntity.itemHandler.setStackInSlot(2, ItemStack.EMPTY);
                            }
                        }
                        pPlayer.awardStat(Stats.ENCHANT_ITEM);
                        if (pPlayer instanceof ServerPlayer player) {
                            CriteriaTriggers.ENCHANTED_ITEM.trigger(player, spellItemSlot, i);
                        }
                        blockEntity.setChanged();
                        this.enchantmentSeed.set(pPlayer.getEnchantmentSeed());
                        this.slotsChanged(null);
                        level.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.1F + 0.9F);
                    }
                });
                return true;
            }
        } else {
            Util.logAndPauseIfInIde(pPlayer.getName() + " pressed invalid button id: " + pId);
            return false;
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
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
        } else if (index >= 0  && index <= 3) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, 4, 40, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private List<Spell> getSpellResultForSlot(Random pRandom, SpellTypes.MarkerTypes spellType, SpellTypes.SpellTiers spellTier) {
        List<Spell> list = Lists.newArrayList();
        for(Spell spell : SpellInit.SPELLS_REGISTRY.get()) {
            if (!spell.isTreasureOnly() && spell.getSpellRarity() != SpellTypes.SpellRarities.NONE) {
                if (spell.getMarkerType() == spellType) {
                    if (spell.getSpellTier() == spellTier) {
                        list.add(spell);
                    }
                }
            }
        }
        return Lists.newArrayList(list.get(pRandom.nextInt(list.size())));
/*        int randInt = new Random().nextInt(10);
        if (randInt <= 5) {
            List<Spell> commonSpells = list.stream().filter(p -> p.getSpellRarity() == SpellTypes.SpellRarities.COMMON).collect(Collectors.toList());
            return Lists.newArrayList(commonSpells.get(pRandom.nextInt(commonSpells.size())));
        } else if (randInt <= 8) {
            List<Spell> uncommonSpells = list.stream().filter(p -> p.getSpellRarity() == SpellTypes.SpellRarities.UNCOMMON).collect(Collectors.toList());
            return Lists.newArrayList(uncommonSpells.get(pRandom.nextInt(uncommonSpells.size())));
        }
        List<Spell> rareSpells = list.stream().filter(p -> p.getSpellRarity() == SpellTypes.SpellRarities.RARE).collect(Collectors.toList());
        return Lists.newArrayList(rareSpells.get(pRandom.nextInt(rareSpells.size())));*/
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed.get();
    }

    public int getMaterialCount() {
        ItemStack itemstack = blockEntity.itemHandler.getStackInSlot(2);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }
}
