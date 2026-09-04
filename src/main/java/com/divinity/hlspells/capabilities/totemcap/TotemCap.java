package com.divinity.hlspells.capabilities.totemcap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class TotemCap implements ITotemCap {

    private static final String LIST_KEY = "items";

    private final ItemStack stack;

    public TotemCap(ItemStack stack) {
        this.stack = stack;
    }

    private TotemData data() {
        return stack.getOrDefault(TotemItemProvider.TOTEM_CAP.get(), TotemData.EMPTY);
    }

    private void set(TotemData data) {
        stack.set(TotemItemProvider.TOTEM_CAP.get(), data);
    }

    private static ListTag unwrap(CompoundTag tag) {
        return tag.getList(LIST_KEY, Tag.TAG_COMPOUND);
    }

    private static CompoundTag wrap(ListTag list) {
        CompoundTag tag = new CompoundTag();
        tag.put(LIST_KEY, list);
        return tag;
    }

    @Override
    public BlockPos getBlockPos() {
        return data().blockPos();
    }

    @Override
    public void setBlockPos(BlockPos pos) {
        TotemData d = data();
        set(new TotemData(pos, d.hasDied(), d.hand(), d.inventory(), d.curios(), d.curiosSlot(), d.curioDied()));
    }

    @Override
    public boolean getHasDied() {
        return data().hasDied();
    }

    @Override
    public void hasDied(boolean hasDied) {
        TotemData d = data();
        set(new TotemData(d.blockPos(), hasDied, d.hand(), d.inventory(), d.curios(), d.curiosSlot(), d.curioDied()));
    }

    @Override
    @Nullable
    public InteractionHand getTotemInHand() {
        return switch (data().hand()) {
            case 1 -> InteractionHand.MAIN_HAND;
            case 2 -> InteractionHand.OFF_HAND;
            default -> null;
        };
    }

    @Override
    public void setTotemInHand(@Nullable InteractionHand hand) {
        int handId = hand == InteractionHand.MAIN_HAND ? 1 : hand == InteractionHand.OFF_HAND ? 2 : 0;
        TotemData d = data();
        set(new TotemData(d.blockPos(), d.hasDied(), handId, d.inventory(), d.curios(), d.curiosSlot(), d.curioDied()));
    }

    @Override
    public ListTag getInventoryNBT() {
        return unwrap(data().inventory());
    }

    @Override
    public void setInventoryNBT(ListTag playerInventory) {
        TotemData d = data();
        set(new TotemData(d.blockPos(), d.hasDied(), d.hand(), wrap(playerInventory), d.curios(), d.curiosSlot(), d.curioDied()));
    }

    @Override
    public ListTag getCuriosNBT() {
        return unwrap(data().curios());
    }

    @Override
    public void setCuriosNBT(ListTag curiosInv) {
        TotemData d = data();
        set(new TotemData(d.blockPos(), d.hasDied(), d.hand(), d.inventory(), wrap(curiosInv), d.curiosSlot(), d.curioDied()));
    }

    @Override
    public int getCuriosSlot() {
        return data().curiosSlot();
    }

    @Override
    public void setCuriosSlot(int curiosSlot) {
        TotemData d = data();
        set(new TotemData(d.blockPos(), d.hasDied(), d.hand(), d.inventory(), d.curios(), curiosSlot, d.curioDied()));
    }

    @Override
    public boolean diedTotemInCurios() {
        return data().curioDied();
    }

    @Override
    public void setDiedTotemInCurios(boolean inCurios) {
        TotemData d = data();
        set(new TotemData(d.blockPos(), d.hasDied(), d.hand(), d.inventory(), d.curios(), d.curiosSlot(), inCurios));
    }
}
