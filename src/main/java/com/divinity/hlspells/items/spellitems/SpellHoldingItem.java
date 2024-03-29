package com.divinity.hlspells.items.spellitems;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.particle.GenerateParticles;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.spells.*;
import com.divinity.hlspells.util.SpellUtils;
import com.divinity.hlspells.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class SpellHoldingItem extends ProjectileWeaponItem {

    private final boolean isSpellBook;
    private boolean wasHolding;


    public SpellHoldingItem(Properties properties, boolean isSpellBook) {
        super(properties);
        this.isSpellBook = isSpellBook;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
        if (isSpellBook) {
            if(allowedIn(pGroup)){
                for (Spell spell : SpellInit.SPELLS_REGISTRY.get()) {
                    ItemStack stack = new ItemStack(this);
                    stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                        if (spell != SpellInit.EMPTY.get())
                            cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.get().getKey(spell)).toString());
                    });
                    pItems.add(stack);
                }
            }


        }
        else super.fillItemCategory(pGroup, pItems);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> text, TooltipFlag pFlag) {
        stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
            List<String> spells = cap.getSpells();
            if (isSpellBook) {
                Spell spell = SpellUtils.getSpell(stack);
                text.add(spell.getDisplayName().withStyle(spell.getSpellType().getTooltipFormatting()));
            }
            else {
                text.add(1, Component.literal(ChatFormatting.AQUA + "Spells: "));
                if (spells.isEmpty()) text.add(Component.literal(ChatFormatting.GRAY + "   Empty"));
                else {
                    spells.forEach(spell -> {
                        Spell currentSpell = SpellUtils.getSpellByID(spell);
                        if (cap.getCurrentSpell().equals(spell)) {
                            text.add(Component.literal(ChatFormatting.BLUE + "   " + currentSpell.getTrueDisplayName()));
                        }
                        else text.add(Component.literal(ChatFormatting.GRAY + "   " + currentSpell.getTrueDisplayName()));
                    });
                }
            }
        });
    }

    @Override
    @ParametersAreNonnullByDefault
    @NotNull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        var capability = itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        this.wasHolding = true;
        Spell spell = SpellUtils.getSpell(itemstack);
        var spells = capability.map(ISpellHolder::getSpells).orElse(null);
        if (spells != null && !spells.isEmpty()) {
            player.startUsingItem(hand);
            if (!world.isClientSide()) {
                if (isSpellBook) {
                    world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 0.7F, 0.7F);
                }
                if (spell != SpellInit.EMPTY.get()) {
                    if (spell.getSpellType() == SpellAttributes.Type.CAST) {
                        switch (spell.getMarkerType()) {
                            case COMBAT: world.playSound(null, player.blockPosition(), SoundInit.CHARGE_COMBAT.get(), SoundSource.PLAYERS, 0.7F, 0.7F);
                            case UTILITY: world.playSound(null, player.blockPosition(), SoundInit.CHARGE_UTILITY.get(), SoundSource.PLAYERS, 0.7F, 0.7F);
                        }
                    }
                }
            }
            return InteractionResultHolder.success(itemstack);
        }
        else return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int count) {
        if (livingEntity instanceof Player player) {
            Spell spell = SpellUtils.getSpell(stack);
            ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());
            if(spell instanceof PhasingII || spell instanceof EffectSpell<?> || spell instanceof DescentII || spell instanceof RespirationSpell) {

            }else{
                try{
                    if(spell instanceof HealingCircleSpell || spell instanceof LightningIII || spell instanceof LureSpell || spell instanceof FlamingCircleSpell || spell instanceof SummonSpell<?> || spell instanceof FrostWallSpell|| spell instanceof IlluminateII) {
                        ResourceLocation fileLocation = new ResourceLocation(HLSpells.MODID + ":functions/large/large_rune_2.mcfunction");
                        GenerateParticles.generateParticleRune(fileLocation, livingEntity, spell.getRune());
                    }else{
                        Random pRandom = new Random();
                        for(BlockPos blockpos : EnchantmentTableBlock.BOOKSHELF_OFFSETS){
                            if (pRandom.nextInt(24) == 0) {
                                livingEntity.getLevel().addParticle(ParticleTypes.ENCHANT, (double)livingEntity.getX(), (double)livingEntity.getY() + 2.0D, (double)livingEntity.getZ(), (double)((float)blockpos.getX() + pRandom.nextFloat()) - 0.5D, (double)((float)blockpos.getY() - pRandom.nextFloat() - 1.0F), (double)((float)blockpos.getZ() + pRandom.nextFloat()) - 0.5D);
                            }
                        }

                        ResourceLocation fileLocation = new ResourceLocation(HLSpells.MODID + ":functions/small/small_rune_2.mcfunction");
                        GenerateParticles.generateParticleRune(fileLocation, livingEntity, spell.getRune());
                    }
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }



            var capability = itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
            if (spell.getSpellType() == SpellAttributes.Type.HELD) {
                    spell.execute(player, stack);


                capability.ifPresent(cap -> {
                    if (cap.getSpellSoundBuffer() == 0) {
                        if (spell instanceof Illuminate || spell instanceof IlluminateII) {
                            player.level.playSound(null, player.blockPosition(), SoundInit.HELD_ILLUMINATE.get(), SoundSource.NEUTRAL, 0.5F, 0.5F);
                            cap.setSpellSoundBuffer(13);
                        }
                        else if (spell.getMarkerType() == SpellAttributes.Marker.COMBAT) {
                            player.level.playSound(null, player.blockPosition(), SoundInit.HELD_COMBAT.get(), SoundSource.NEUTRAL, 0.7F, 0.7F);
                            cap.setSpellSoundBuffer(47);
                        }
                        else if (spell.getMarkerType() == SpellAttributes.Marker.UTILITY) {
                            player.level.playSound(null, player.blockPosition(), SoundInit.HELD_UTILITY.get(), SoundSource.NEUTRAL, 0.7F, 0.7F);
                            cap.setSpellSoundBuffer(46);
                        }
                    }
                    else cap.setSpellSoundBuffer(cap.getSpellSoundBuffer() - 1);
                });
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int power) {
        if (entity instanceof Player player) {
            var capability = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
            capability.ifPresent(cap -> cap.setSpellSoundBuffer(0));
            this.wasHolding = false;

            Spell spell = SpellUtils.getSpell(stack);

            if(spell instanceof PhasingII){
                player.setInvulnerable(false);
                player.setInvisible(false);
            }
//            if(spell instanceof DescentII){
//                player.setForcedPose(Pose.STANDING);
//            }
            if (this.castTimeCondition(player, stack)) {
                if (spell.getSpellType() == SpellAttributes.Type.CAST) {
                    world.playSound(null, player.blockPosition(), spell.getSpellSound(), SoundSource.NEUTRAL, 0.7F, 0.7F);
                    spell.execute(player, stack);
                }
                Util.doParticles(player);
                if (!world.isClientSide()) {
                    capability.filter(p -> !(p.getSpells().isEmpty())).ifPresent(cap -> {
                            player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 20));
                        if (stack.getItem() instanceof StaffItem item) {
                            if (item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.COMBAT) {
                                player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 25));
                            }
                            else if (!item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.UTILITY) {
                                player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 25));
                            }
                        }
                        if (stack.getItem() instanceof StaffItem item) {
                            if (!item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.COMBAT) {
                                player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 10));
                            }
                            else if (item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.UTILITY) {
                                player.getCooldowns().addCooldown(stack.getItem(), (int) (HLSpells.CONFIG.cooldownDuration.get() * 10));
                            }
                        }
                    });
                }
            }else{
                player.playSound(SoundInit.MISCAST_SOUND.get(), 0.7f, 0.7f);
            }
        }
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        if (EnchantedBookItem.getEnchantments(book).size() > 0) {
            for (int i = 0; i < EnchantedBookItem.getEnchantments(book).size(); i++) {
                CompoundTag tag = EnchantedBookItem.getEnchantments(book).getCompound(i);
                ResourceLocation enchantment = EnchantmentHelper.getEnchantmentId(tag);
                if (enchantment != null) {
                    switch (enchantment.toString()) {
                        case "minecraft:mending":
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, stack) == 0) {
                                return !isSpellBook || stack.getItem() instanceof StaffItem;
                            }
                        case "minecraft:unbreaking":
                            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) <= EnchantmentHelper.getEnchantmentLevel(tag)) {
                                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) != 3) {
                                    return !isSpellBook || stack.getItem() instanceof StaffItem;
                                }
                            }
                        case "hlspells:soul_bond":
                            if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.SOUL_BOND.get(), stack) == 0) {
                                return !isSpellBook || stack.getItem() instanceof StaffItem;
                            }
                        break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        var spells = pStack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getSpells).orElse(null);
        return isSpellBook && SpellUtils.getSpell(pStack) != SpellInit.EMPTY.get() || !isSpellBook && spells != null && !spells.isEmpty() || super.isFoil(pStack);
    }

    // Responsible for syncing capability to client side
    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag capTag = new CompoundTag();
        var spellHolder = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (spellHolder.isPresent()) {
            spellHolder.ifPresent(cap -> capTag.put("spellHolder", stack.serializeNBT()));
        }
        CompoundTag stackTag = stack.getTag();
        if (capTag.isEmpty()) return stackTag;
        else if (stackTag == null) stackTag = new CompoundTag();
        else stackTag = stackTag.copy(); // Because we don't actually want to add this data to the server side ItemStack
        stackTag.put("spellCap", capTag);
        return stackTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        stack.setTag(nbt);
        if (nbt != null && nbt.contains("spellCap")) {
            CompoundTag capTags = nbt.getCompound("spellCap");
            if (capTags.contains("spellHolder")) {
                stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> stack.deserializeNBT(nbt));
            }
        }
    }

    @Override @NotNull public Predicate<ItemStack> getAllSupportedProjectiles() { return ARROW_ONLY; }

    @Override @NotNull public UseAnim getUseAnimation(@NotNull ItemStack pStack) { return isSpellBook ? UseAnim.CROSSBOW : UseAnim.BOW; }

    @Override @Nullable public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) { return new SpellHolderProvider(); }

    @Override public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) { return false; }

    @Override public int getDefaultProjectileRange() { return 8; }

    @Override public int getUseDuration(@NotNull ItemStack pStack) { return 72000; }

    @Override public boolean isEnchantable(@NotNull ItemStack pStack) { return isSpellBook; }

    @Override public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }

    @SuppressWarnings("all")
    public boolean isSpellBook() {
        return isSpellBook;
    }

    /**
     * Returns true if wand or staff
     */
    public boolean isWand() {
        return !isSpellBook;
    }

    public boolean isWasHolding() {
        return this.wasHolding;
    }

    public void setWasHolding(boolean wasHolding) {
        this.wasHolding = wasHolding;
    }

    private boolean castTimeCondition(Player player, ItemStack stack) {
        if (stack.getItem() instanceof StaffItem item) {
            return player.getUseItemRemainingTicks() < (72000 - (item.getCastDelay()));
        }
        else if (!this.isSpellBook) {
            return player.getUseItemRemainingTicks() < (72000 - (HLSpells.CONFIG.spellCastTime.get() * 20));
        }
        return player.getUseItemRemainingTicks() < 71988;
    }

}
