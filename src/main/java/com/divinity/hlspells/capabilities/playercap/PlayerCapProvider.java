package com.divinity.hlspells.capabilities.playercap;

import net.minecraft.nbt.*;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("all")
public class PlayerCapProvider implements ICapabilitySerializable<CompoundTag> {

    public static Capability<IPlayerCap> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    private PlayerCap playerCap = null;
    private final LazyOptional<IPlayerCap> instance = LazyOptional.of(this::createPlayerCap);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == PLAYER_CAP ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return this.getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                if (cap.getEffect() != null) {
                    tag.putInt("effect", MobEffect.getId(cap.getEffect()));
                    tag.putInt("effectDuration", cap.getEffectDuration());
                    tag.putInt("effectAmplifier", cap.getEffectAmplifier());
                }
                tag.putInt("soulBondItemsSize", cap.getSoulBondItems().size());
                ListTag slotsNBT = new ListTag();
                ListTag stacksNBT = new ListTag();
                cap.getSoulBondItems().keySet().forEach(id -> slotsNBT.add(IntTag.valueOf(id)));
                cap.getSoulBondItems().values().forEach(stack -> stacksNBT.add(stack.save(new CompoundTag())));
                tag.put("slotIds", slotsNBT);
                tag.put("stacks", stacksNBT);
                tag.putInt("spellTimer", cap.getSpellTimer());
                tag.putInt("spellXpTickCounter", cap.getSpellXpTickCounter());
                tag.putInt("durabilityTickCounter", cap.getDurabilityTickCounter());
            });
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (instance.isPresent()) {
            instance.ifPresent(cap -> {
                int effect = nbt.getInt("effect");
                if (effect != 0) {
                    cap.setEffect(MobEffect.byId(nbt.getInt("effect")));
                    cap.setEffectDuration(nbt.getInt("effectDuration"));
                    cap.setEffectAmplifier(nbt.getInt("effectAmplifier"));
                }
                int soulBondItemsSize = nbt.getInt("soulBondItemsSize");
                ListTag slotsNBT = nbt.getList("slotIds", 0);
                ListTag stacksNBT = nbt.getList("stacks", 0);
                for (int i = 0; i < soulBondItemsSize; i++) {
                    Tag slot = slotsNBT.get(i);
                    Tag stack = stacksNBT.get(i);
                    if (slot instanceof IntTag intTag && stack instanceof CompoundTag compoundTag) {
                        cap.addSoulBondItem(intTag.getAsInt(), ItemStack.of(compoundTag));
                    }
                }
                cap.setSpellTimer(nbt.getInt("spellTimer"));
                cap.setSpellXpTickCounter(nbt.getInt("spellXpTickCounter"));
                cap.setDurabilityTickCounter(nbt.getInt("durabilityTickCounter"));
            });
        }
    }

    @Nonnull private IPlayerCap createPlayerCap() {
        return playerCap == null ? new PlayerCap() : playerCap;
    }
}
