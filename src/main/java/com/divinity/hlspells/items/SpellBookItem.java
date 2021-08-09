package com.divinity.hlspells.items;

import com.divinity.hlspells.enchantments.ISpell;
import com.divinity.hlspells.init.EnchantmentInit;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spell.SpellBookObject;
import com.divinity.hlspells.spell.SpellInstance;
import com.divinity.hlspells.init.SpellBookInit;
import com.divinity.hlspells.spells.SpellActions;
import com.divinity.hlspells.util.SpellUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.registries.ForgeRegistries;
import static com.divinity.hlspells.setup.client.ClientSetup.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class SpellBookItem extends ShootableItem
{
    public static boolean isHeldActive = false;
    private List<SpellInstance> spell = new ArrayList<>();

    public SpellBookItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        ItemStack stack = new ItemStack(this);
        return SpellUtils.setSpellBook(stack, SpellBookInit.EMPTY.get());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return SpellUtils.getSpellBook(stack) == SpellBookInit.EMPTY.get() && enchantment instanceof ISpell;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag)
    {
        SpellUtils.addSpellBookTooltip(stack, text, 1.0F);
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return super.isFoil(stack) || SpellUtils.getSpellBook(stack) != SpellBookInit.EMPTY.get();
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks) {

        if (this.allowdedIn(group)) {
            for (SpellBookObject spellBookObject : SpellBookInit.SPELL_BOOK_REGISTRY.get())
            {
                stacks.add(SpellUtils.setSpellBook(new ItemStack(this), spellBookObject));
            }
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles()
    {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange()
    {
        return 8;
    }

    static final SoundEvent evokerCastSpell = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.evoker.cast_spell"));

    @SuppressWarnings("all")
    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int power)
    {
        System.out.println(stack.getOrCreateTag());
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            isHeldActive = false;

            if (playerEntity.getUseItemRemainingTicks() < 71988)
            {
                if (!playerEntity.getCommandSenderWorld().isClientSide())
                {
                    RunSpells.doCastSpell(playerEntity, world, stack);
                    if (evokerCastSpell != null) playerEntity.level.playSound(null, playerEntity.blockPosition(), evokerCastSpell, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }

                if (playerEntity.getCommandSenderWorld().isClientSide())
                {
                    SpellActions.doParticles(playerEntity);

                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int value, boolean bool)
    {
        if (stack.getEnchantmentTags().size() > 1)
        {
            for (int i = 0; i < stack.getEnchantmentTags().size() && stack.getEnchantmentTags().size() > 1; i++)
            {
                stack.getEnchantmentTags().remove(i);
            }
        }

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity =  (PlayerEntity) entity;
            if (isHeldActive)
            {
                if (playerEntity.getMainHandItem().getItem() instanceof SpellBookItem || playerEntity.getOffhandItem().getItem() instanceof SpellBookItem)
                {

                    if (SpellUtils.getSpellBook(playerEntity.getMainHandItem().getStack()).getSpells() == spell ||
                            SpellUtils.getSpellBook(playerEntity.getOffhandItem().getStack()).getSpells() == spell)
                    {
                        return;
                    }
                }
                isHeldActive = false;
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return true;
    }

    @Override
    public ActionResult<ItemStack> use (World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        this.spell = SpellUtils.getSpellBook(itemstack).getSpells();
        isHeldActive = true;
        
        if (!playerIn.level.isClientSide()) 
        {
            if (frameTwo)
            {
                if (pageTurnSound != null && playerIn.level != null)
                    playerIn.level.playSound(null, playerIn.blockPosition(), pageTurnSound, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                frameTwo = false;
            }

            if (frameThree) 
            {
                if (SpellUtils.getSpellBook(itemstack).containsSpell(p -> p.getSpell().getCategory() == SpellType.HELD))
                {
                    if (evokerPrepareAttack != null && playerIn.level != null)
                        playerIn.level.playSound(null, playerIn.blockPosition(), evokerPrepareAttack, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                } 
                
                else if (SpellUtils.getSpellBook(itemstack).containsSpell(p -> p.getSpell().getCategory() == SpellType.CAST)) 
                {
                    if (evokerPrepareSummon != null && playerIn.level != null)
                        playerIn.level.playSound(null, playerIn.blockPosition(), evokerPrepareSummon, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                }
                frameThree = false;
            }
        }
        return ActionResult.success(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack)
    {
        return UseAction.CROSSBOW;
    }
}
