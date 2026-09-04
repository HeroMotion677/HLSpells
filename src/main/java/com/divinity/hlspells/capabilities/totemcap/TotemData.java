package com.divinity.hlspells.capabilities.totemcap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record TotemData(BlockPos blockPos, boolean hasDied, int hand, CompoundTag inventory, CompoundTag curios,
                        int curiosSlot, boolean curioDied) {

    public static final TotemData EMPTY = new TotemData(BlockPos.ZERO, false, 0, new CompoundTag(), new CompoundTag(), 0, false);

    public static final Codec<TotemData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BlockPos.CODEC.fieldOf("blockPos").forGetter(TotemData::blockPos),
            Codec.BOOL.fieldOf("hasDied").forGetter(TotemData::hasDied),
            Codec.INT.fieldOf("hand").forGetter(TotemData::hand),
            CompoundTag.CODEC.fieldOf("playerInv").forGetter(TotemData::inventory),
            CompoundTag.CODEC.fieldOf("curiosInv").forGetter(TotemData::curios),
            Codec.INT.fieldOf("curiosSlot").forGetter(TotemData::curiosSlot),
            Codec.BOOL.fieldOf("curiosDied").forGetter(TotemData::curioDied)
    ).apply(inst, TotemData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, TotemData> STREAM_CODEC = StreamCodec.of(
            (buffer, data) -> {
                BlockPos.STREAM_CODEC.encode(buffer, data.blockPos);
                buffer.writeBoolean(data.hasDied);
                buffer.writeVarInt(data.hand);
                buffer.writeNbt(data.inventory);
                buffer.writeNbt(data.curios);
                buffer.writeVarInt(data.curiosSlot);
                buffer.writeBoolean(data.curioDied);
            },
            buffer -> new TotemData(BlockPos.STREAM_CODEC.decode(buffer), buffer.readBoolean(), buffer.readVarInt(),
                    buffer.readNbt(), buffer.readNbt(), buffer.readVarInt(), buffer.readBoolean()));
}
