package com.divinity.hlspells.items;

import com.divinity.hlspells.init.SpellInit;
import com.divinity.hlspells.items.capabilities.wandcap.WandItemProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellType;
import com.divinity.hlspells.spells.RunSpells;
import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static com.divinity.hlspells.items.SpellBookItem.evokerCastSpell;
import static com.divinity.hlspells.setup.client.ClientSetup.*;


public class WandItem extends ShootableItem
{
    public static boolean isWandHeldActive = false;

    public WandItem (Properties properties)
    {
        super(properties);
    }

    @Override
    public ItemStack getDefaultInstance()
    {
        return new ItemStack(this);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> stacks)
    {
        if (this.allowdedIn(group))
        {
            stacks.add(new ItemStack(this));
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> text, ITooltipFlag flag)
    {
        text.add(1, new StringTextComponent(TextFormatting.GOLD + "Spells: "));
        stack.getCapability(WandItemProvider.WAND_CAP,  null).ifPresent(cap ->
        {
            if (cap.getSpells().size() == 0)
            {
                text.add(new StringTextComponent(TextFormatting.GRAY + "   Empty"));
            }
            else
            {
                cap.getSpells().forEach(c ->
                {
                    for (RegistryObject<Spell> spell : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
                    {
                        ResourceLocation location = spell.get().getRegistryName();
                        if (location != null && location.toString().equals(c))
                        {
                            if (cap.getSpells().get(cap.getCurrentSpellCycle()).equals(c))
                            {
                                text.add(new StringTextComponent(TextFormatting.BLUE + "   " + spell.get().getTrueDisplayName()));
                            }
                            else
                            {
                                text.add(new StringTextComponent(TextFormatting.GRAY + "   " + spell.get().getTrueDisplayName()));
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);;
        isWandHeldActive = true;

        itemstack.getCapability(WandItemProvider.WAND_CAP, null).filter(p -> p.getSpells().size() > 0).ifPresent(cap ->
        {
            for (RegistryObject<Spell> spell : SpellInit.SPELLS_DEFERRED_REGISTER.getEntries())
            {
                ResourceLocation location = spell.get().getRegistryName();
                if (location != null && cap.getSpells().get(cap.getCurrentSpellCycle()).equals(location.toString()))
                {
                    if (!playerIn.level.isClientSide())
                    {
                        if (wandFrameThree)
                        {
                            if (spell.get().getCategory() == SpellType.CAST)
                            {
                                if (evokerPrepareAttack != null && playerIn.level != null)
                                    playerIn.level.playSound(null, playerIn.blockPosition(), evokerPrepareAttack, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                            }

                            else if (spell.get().getCategory() == SpellType.HELD)
                            {
                                if (evokerPrepareSummon != null && playerIn.level != null)
                                    playerIn.level.playSound(null, playerIn.blockPosition(), evokerPrepareSummon, SoundCategory.NEUTRAL, 0.6F, 1.0F);
                            }
                            wandFrameThree = false;
                            break;
                        }
                    }
                }
            }
        });
        return ActionResult.success(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
    {
        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity =  (PlayerEntity) entity;
            if (isWandHeldActive)
            {
                if (playerEntity.getMainHandItem().getItem() instanceof WandItem || playerEntity.getOffhandItem().getItem() instanceof WandItem)
                {
                    return;
                }
                isWandHeldActive = false;
            }
        }
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_)
    {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_)
    {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int p_77615_4_)
    {
        stack.getCapability(WandItemProvider.WAND_CAP, null).ifPresent(p -> {
            System.out.println(p.getSpells());
        });

        if (entity instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            if (evokerCastSpell != null) playerEntity.level.playSound(null, playerEntity.blockPosition(), evokerCastSpell, SoundCategory.NEUTRAL, 0.6F, 1.0F);
            isWandHeldActive = false;

            if (playerEntity.getUseItemRemainingTicks() < 71988)
            {
                if (!playerEntity.getCommandSenderWorld().isClientSide())
                {
                    RunSpells.doCastSpell(playerEntity, world, stack);
                }

                if (playerEntity.getCommandSenderWorld().isClientSide())
                {
                    SpellActions.doParticles(playerEntity);
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_)
    {
        return super.isFoil(p_77636_1_);
    }

    @Override
    public Rarity getRarity(ItemStack p_77613_1_)
    {
        return super.getRarity(p_77613_1_);
    }

    @Override
    public boolean isRepairable(ItemStack stack)
    {
        return super.isRepairable(stack);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return false;
    }
}
