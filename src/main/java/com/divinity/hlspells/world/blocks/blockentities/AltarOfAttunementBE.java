package com.divinity.hlspells.world.blocks.blockentities;

import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.world.blocks.blockentities.inventory.AltarOfAttunementMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

@SuppressWarnings("NullableProblems")
public class AltarOfAttunementBE extends EnchantingTableBlockEntity implements MenuProvider {

    public final ItemStackHandler itemHandler = createHandler();

    public AltarOfAttunementBE(BlockPos pWorldPosition, BlockState pBlockState) {
        super(pWorldPosition, pBlockState);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);
        if (pTag.contains("Inventory")) {
            itemHandler.deserializeNBT(provider, pTag.getCompound("Inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        pTag.put("Inventory", itemHandler.serializeNBT(provider));
        super.saveAdditional(pTag, provider);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return this.level != null ? new AltarOfAttunementMenu(pContainerId, pInventory, pPlayer, this, ContainerLevelAccess.create(this.level, this.getBlockPos())) : null;
    }

    @Nullable @Override public Packet<ClientGamePacketListener> getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    @Override public BlockEntityType<?> getType() {
        return BlockInit.ALTAR_BE.get();
    }

    @Override public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override public Component getDisplayName() {
        return Component.translatable("container.hlspells.altar_of_attunement.transfer");
    }

    public void dropContents() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        if (this.level != null) Containers.dropContents(this.level, this.getBlockPos(), inventory);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(4) {
            @Override protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }
}
