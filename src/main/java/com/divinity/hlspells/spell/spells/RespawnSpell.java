package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.Optional;

public class RespawnSpell extends Spell {

    public RespawnSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    protected SpellConsumer<Player> getAction() {
        return p -> {
            if (p instanceof ServerPlayer player && player.getServer() != null) {

                if (getLodestonePosition(p.getUseItem().getOrCreateTag()) == null) {
                    ServerLevel dimLevel = player.getServer().getLevel(player.getRespawnDimension());
                    BlockPos pos = player.getRespawnPosition();
                    if (dimLevel != null && pos != null) {
                        player.teleportTo(dimLevel, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
                        return true;
                    }
                    return false;
                }else{
                    CompoundTag compoundtag = p.getUseItem().getOrCreateTag();
                    BlockPos pos = NbtUtils.readBlockPos(compoundtag.getCompound("LodestonePos"));
                    Optional<ResourceKey<Level>> optional = getLodestoneDimension(compoundtag);
                    ServerLevel dimLevel = p.getServer().getLevel(optional.get());
                    if (dimLevel != null && pos != null) {
                        player.teleportTo(dimLevel, pos.getX(), pos.getY(), pos.getZ(), player.getYRot(), player.getXRot());
                        return true;
                    }
                }

            }
            return false;
        };
    }

    @Nullable
    public static GlobalPos getLodestonePosition(CompoundTag pTag) {
        boolean flag = pTag.contains("LodestonePos");
        boolean flag1 = pTag.contains("LodestoneDimension");
        if (flag && flag1) {
            Optional<ResourceKey<Level>> optional = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pTag.get("LodestoneDimension")).result();;
            if (optional.isPresent()) {
                BlockPos blockpos = NbtUtils.readBlockPos(pTag.getCompound("LodestonePos"));
                return GlobalPos.of(optional.get(), blockpos);
            }
        }

        return null;
    }

    private static Optional<ResourceKey<Level>> getLodestoneDimension(CompoundTag pCompoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get("LodestoneDimension")).result();
    }
}

