package com.divinity.hlspells.capabilities.spellholdercap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record SpellHolderData(List<String> spells, int currentSpellCycle, int spellSoundBuffer) {

    public static final SpellHolderData EMPTY = new SpellHolderData(List.of(), 0, 0);

    public static final Codec<SpellHolderData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.listOf().fieldOf("spells").forGetter(SpellHolderData::spells),
            Codec.INT.fieldOf("currentSpellCycle").forGetter(SpellHolderData::currentSpellCycle),
            Codec.INT.fieldOf("spellSoundBuffer").forGetter(SpellHolderData::spellSoundBuffer)
    ).apply(inst, SpellHolderData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpellHolderData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), SpellHolderData::spells,
            ByteBufCodecs.VAR_INT, SpellHolderData::currentSpellCycle,
            ByteBufCodecs.VAR_INT, SpellHolderData::spellSoundBuffer,
            SpellHolderData::new);
}
