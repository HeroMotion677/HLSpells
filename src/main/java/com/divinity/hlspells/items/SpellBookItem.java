package com.divinity.hlspells.items;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.wandcap.ISpellHolder;
import com.divinity.hlspells.items.capabilities.wandcap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spells.SpellActions;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class SpellBookItem extends ShootableItem {

    public SpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return SpellUtils.getSpell(stack) == SpellInit.EMPTY.get() && enchantment instanceof ISpell;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag) {
        Spell spell = SpellUtils.getSpell(stack);
        text.add(spell.getDisplayName().withStyle(spell.getType().getTooltipFormatting()));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return SpellUtils.getSpell(stack) != SpellInit.EMPTY.get();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (Spell spell : SpellInit.SPELLS_REGISTRY.get()) {
                ItemStack stack = new ItemStack(this);
                stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.addSpell(spell.getRegistryName().toString()));
                stacks.add(stack);
            }
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new SpellHolderProvider();
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int power) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(false));
            if (player.getUseItemRemainingTicks() < 71988) {
                if (!player.getCommandSenderWorld().isClientSide()) {
                    RunSpells.doCastSpell(player, world, stack);
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
                SpellActions.doParticles(player);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int value, boolean bool) {
        if (stack.getEnchantmentTags().size() > 1) {
            for (int i = 0; i < stack.getEnchantmentTags().size() && stack.getEnchantmentTags().size() > 1; i++) {
                stack.getEnchantmentTags().remove(i);
            }
        }
        if (entity instanceof PlayerEntity && stack.getItem() instanceof SpellBookItem) {
            PlayerEntity player = (PlayerEntity) entity;
            stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> {
                if (cap.isHeldActive()) {
                    //TODO currently held spell works when just held in item no need to right click to activate fix it
                    for (Hand hand : Hand.values()) {
                        ItemStack stack1 = player.getItemInHand(hand);
                        String spell = stack1.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).map(ISpellHolder::getCurrentSpell).orElse("");
                        if (spell.equals(cap.getCurrentSpell()))
                            return;
                    }
                    cap.setHeldActive(false);
                }
            });
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand handIn) {
        ItemStack itemstack = player.getItemInHand(handIn);
        player.startUsingItem(handIn);
        itemstack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(cap -> cap.setHeldActive(true));

        if (!world.isClientSide()) {
            if (player.getUseItemRemainingTicks() < 71997 && player.getUseItemRemainingTicks() >= 71994) {
                world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            }

            if (player.getUseItemRemainingTicks() < 71994 && player.getUseItemRemainingTicks() >= 71991) {
                Spell spell = SpellUtils.getSpell(itemstack);
                if (spell.test(s -> s.getType() == SpellType.HELD)) {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                } else if (spell.test(s -> s.getType() == SpellType.CAST)) {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
            }
        }
        return ActionResult.success(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    // Responsible for syncing capability to client side gui's
    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT capTag = new CompoundNBT();
        LazyOptional<ISpellHolder> spellHolder = stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP);
        if (spellHolder.isPresent()) {
            spellHolder.ifPresent(cap -> capTag.put("spellHolder", Objects.requireNonNull(SpellHolderProvider.SPELL_HOLDER_CAP.writeNBT(spellHolder.orElseThrow(RuntimeException::new), null))));
        }
        CompoundNBT stackTag = stack.getTag();
        if (capTag.isEmpty()) {
            return stackTag;
        }
        if (stackTag == null) {
            stackTag = new CompoundNBT();
        } else {
            stackTag = stackTag.copy(); //Because we dont actually want to add this data to the server side ItemStack
        }
        stackTag.put("spellCap", capTag);
        return stackTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        stack.setTag(nbt);
        if (nbt != null && nbt.contains("spellCap")) {
            CompoundNBT capTags = nbt.getCompound("spellCap");
            if (capTags.contains("spellHolder")) {
                stack.getCapability(SpellHolderProvider.SPELL_HOLDER_CAP).ifPresent(spellHolder -> SpellHolderProvider.SPELL_HOLDER_CAP.readNBT(spellHolder, null, capTags.get("spellHolder")));
            }
            nbt.remove("spellCap");
        }
    }
}
