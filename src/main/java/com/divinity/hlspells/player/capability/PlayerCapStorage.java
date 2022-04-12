package com.divinity.hlspells.player.capability;

//import net.minecraft.world.item.ItemStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.nbt.IntTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.core.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.util.INBTSerializable;
//
//import javax.annotation.Nullable;
//
//public class PlayerCapStorage implements INBTSerializable<CompoundTag> {
//
//    @Nullable
//    @Override
//    public Tag writeNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side) {
//        CompoundTag tag = new CompoundTag();
//        if (instance.getEffect() != null) {
//            tag.putInt("effect", MobEffect.getId(instance.getEffect()));
//            tag.putInt("effectDuration", instance.getEffectDuration());
//            tag.putInt("effectAmplifier", instance.getEffectAmplifier());
//        }
//        tag.putInt("soulBondItemsSize", instance.getSoulBondItems().size());
//        ListTag slotsNBT = new ListTag();
//        ListTag stacksNBT = new ListTag();
//        instance.getSoulBondItems().keySet().forEach(id -> slotsNBT.add(IntTag.valueOf(id)));
//        instance.getSoulBondItems().values().forEach(stack -> stacksNBT.add(stack.save(new CompoundTag())));
//        tag.put("slotIds", slotsNBT);
//        tag.put("stacks", stacksNBT);
//        return tag;
//    }
//
//    @Override
//    public void readNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side, Tag nbt) {
//        CompoundTag tag = (CompoundTag) nbt;
//        int effect = tag.getInt("effect");
//        if (effect != 0) {
//            instance.setEffect(MobEffect.byId(tag.getInt("effect")));
//            instance.setEffectDuration(tag.getInt("effectDuration"));
//            instance.setEffectAmplifier(tag.getInt("effectAmplifier"));
//        }
//        int soulBondItemsSize = tag.getInt("soulBondItemsSize");
//        ListTag slotsNBT = tag.getList("slotIds", 0);
//        ListTag stacksNBT = tag.getList("stacks", 0);
//        for (int i = 0; i < soulBondItemsSize; i++) {
//            Tag slot = slotsNBT.get(i);
//            Tag stack = stacksNBT.get(i);
//            if (slot instanceof IntTag && stack instanceof CompoundTag) {
//                instance.addSoulBondItem(((IntTag) slot).getAsInt(), ItemStack.of(((CompoundTag) stack)));
//            }
//        }
//    }
//}
