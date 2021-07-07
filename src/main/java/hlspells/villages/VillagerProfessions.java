package com.heromotion.hlspells.villages;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.village.PointOfInterestType;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class VillagerProfessions extends VillagerProfession {

    private final List<Supplier<SoundEvent>> soundEventSuppliers;

    @SafeVarargs
    public VillagerProfessions(String nameIn, PointOfInterestType pointOfInterestIn, ImmutableSet<Item> specificItemsIn,
                               ImmutableSet<Block> relatedWorldBlocksIn, Supplier<SoundEvent>... soundEventSuppliers) {
        super(nameIn, pointOfInterestIn, specificItemsIn, relatedWorldBlocksIn, null);
        this.soundEventSuppliers = Arrays.asList(soundEventSuppliers);
    }

    @Nullable
    @Override
    public SoundEvent getWorkSound() {
        int n = ThreadLocalRandom.current().nextInt(soundEventSuppliers.size());
        return soundEventSuppliers.get(n).get();
    }
}
