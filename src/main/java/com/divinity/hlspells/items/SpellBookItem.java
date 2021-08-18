package com.divinity.hlspells.items;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellInstance;
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
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class SpellBookItem extends ShootableItem {
    public static boolean isHeldActive = false;
    private List<SpellInstance> spell = new ArrayList<>();

    public SpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        return SpellUtils.setSpellBook(stack, SpellBookInit.EMPTY.get());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return SpellUtils.getSpellBook(stack) == SpellBookInit.EMPTY.get() && enchantment instanceof ISpell;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag) {
        SpellUtils.addSpellBookTooltip(stack, text);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || SpellUtils.getSpellBook(stack) != SpellBookInit.EMPTY.get();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(group)) {
            for (SpellBookObject spellBookObject : SpellBookInit.SPELL_BOOK_REGISTRY.get()) {
                stacks.add(SpellUtils.setSpellBook(new ItemStack(this), spellBookObject));
            }
        }
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
            PlayerEntity playerEntity = (PlayerEntity) entity;
            isHeldActive = false;
            if (playerEntity.getUseItemRemainingTicks() < 71988) {
                if (!playerEntity.getCommandSenderWorld().isClientSide()) {
                    RunSpells.doCastSpell(playerEntity, world, stack);
                    playerEntity.level.playSound(null, playerEntity.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
                if (playerEntity.getCommandSenderWorld().isClientSide()) {
                    SpellActions.doParticles(playerEntity);
                }
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

        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (isHeldActive) {
                if (playerEntity.getMainHandItem().getItem() instanceof SpellBookItem || playerEntity.getOffhandItem().getItem() instanceof SpellBookItem) {
                    if (SpellUtils.getSpellBook(playerEntity.getMainHandItem().getStack()).getSpells() == spell ||
                            SpellUtils.getSpellBook(playerEntity.getOffhandItem().getStack()).getSpells() == spell) {
                        return;
                    }
                }
                isHeldActive = false;
            }
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
        this.spell = SpellUtils.getSpellBook(itemstack).getSpells();
        isHeldActive = true;

        if (!world.isClientSide()) {
            if (player.getUseItemRemainingTicks() < 71997 && player.getUseItemRemainingTicks() >= 71994) {
                world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            }

            if (player.getUseItemRemainingTicks() < 71994 && player.getUseItemRemainingTicks() >= 71991) {
                if (SpellUtils.getSpellBook(itemstack).containsSpell(p -> p.getSpell().getType() == SpellType.HELD)) {
                    world.playSound(null, player.blockPosition(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                } else if (SpellUtils.getSpellBook(itemstack).containsSpell(p -> p.getSpell().getType() == SpellType.CAST)) {
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
}
