package com.divinity.hlspells.items.capabilities.totemcap;

//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.nbt.NbtUtils;
//import net.minecraft.core.Direction;
//import net.minecraft.world.InteractionHand;
//import net.minecraftforge.common.capabilities.Capability;
//
//import javax.annotation.Nullable;
//
//public class TotemItemStorage implements Capability.IStorage<ITotemCap> {
//    @Nullable
//    @Override
//    public Tag writeNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side) {
//        CompoundTag tag = new CompoundTag();
//        tag.put("blockPos", NbtUtils.writeBlockPos(instance.getBlockPos()));
//        tag.putBoolean("hasDied", instance.getHasDied());
//        int hand = 0;
//        InteractionHand totemInHand = instance.getTotemInHand();
//        if (totemInHand == InteractionHand.MAIN_HAND) {
//            hand = 1;
//        } else if (totemInHand == InteractionHand.OFF_HAND) {
//            hand = 2;
//        }
//        tag.putInt("hand", hand);
//        tag.put("playerInv", instance.getInventoryNBT());
//        tag.put("curiosInv", instance.getCuriosNBT());
//        tag.putInt("curiosSlot", instance.getCuriosSlot());
//        tag.putBoolean("curiosDied", instance.diedTotemInCurios());
//        return tag;
//    }
//
//    @Override
//    public void readNBT(Capability<ITotemCap> capability, ITotemCap instance, Direction side, Tag nbt) {
//        CompoundTag tag = (CompoundTag) nbt;
//        instance.setBlockPos(NbtUtils.readBlockPos(tag.getCompound("blockPos")));
//        instance.hasDied(tag.getBoolean("hasDied"));
//        int hand = tag.getInt("hand");
//        if (hand == 1) {
//            instance.setTotemInHand(InteractionHand.MAIN_HAND);
//        } else if (hand == 2) {
//            instance.setTotemInHand(InteractionHand.OFF_HAND);
//        } else {
//            instance.setTotemInHand(null);
//        }
//        instance.setInventoryNBT(tag.getList("playerInv", 0));
//        instance.setCuriosNBT(tag.getList("curiosInv", 0));
//        instance.setCuriosSlot(tag.getInt("curiosSlot"));
//        instance.setDiedTotemInCurios(tag.getBoolean("curiosDied"));
//    }
//}