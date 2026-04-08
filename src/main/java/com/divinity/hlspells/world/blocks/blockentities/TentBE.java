package com.divinity.hlspells.world.blocks.blockentities;

import com.divinity.hlspells.setup.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TentBE extends BlockEntity {



    private BlockPos bed;
    public TentBE(BlockPos pPos, BlockState pBlockState) {
        super(BlockInit.ORANGE_TENT_BE.get(), pPos, pBlockState);
    }


    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (bed != null) {
            pTag.put("bed", NbtUtils.writeBlockPos(bed));
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("bed")) {
            bed = NbtUtils.readBlockPos(pTag.getCompound("bed"));
        } else {
            bed = null;
        }
    }

    public void setPrevBed(BlockPos bed){
        this.bed = bed;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }
    public BlockPos getBed() {
        return bed;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BlockInit.ORANGE_TENT_BE.get();
    }
}
