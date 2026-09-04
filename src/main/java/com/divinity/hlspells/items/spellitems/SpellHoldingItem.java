package com.divinity.hlspells.items.spellitems;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.ISpellHolder;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.particle.GenerateParticles;
import com.divinity.hlspells.setup.init.EnchantmentInit;
import com.divinity.hlspells.setup.init.ItemInit;
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
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class SpellHoldingItem extends ProjectileWeaponItem {
	private double currentCastTime = 0;
	private final boolean isSpellBook;
	private boolean wasHolding;

	public SpellHoldingItem(Properties properties, boolean isSpellBook) {
		super(properties);
		this.isSpellBook = isSpellBook;
	}


	@Override
	@ParametersAreNonnullByDefault
	public void appendHoverText(ItemStack stack, TooltipContext pContext, List<Component> text, TooltipFlag pFlag) {
		SpellHolderProvider.get(stack).ifPresent(cap -> {
			List<String> spells = cap.getSpells();
			if (isSpellBook) {
				Spell spell = SpellUtils.getSpell(stack);
				text.add(spell.getDisplayName().withStyle(spell.getSpellType().getTooltipFormatting()));
			} else {
				text.add(1, Component.literal(ChatFormatting.BLUE + "Spells: "));
				if (spells.isEmpty())
					text.add(Component.literal(ChatFormatting.GRAY + "   Empty"));
				else {
					spells.forEach(spell -> {
						Spell currentSpell = SpellUtils.getSpellByID(spell);
						if (cap.getCurrentSpell().equals(spell)) {
							text.add(Component.literal(ChatFormatting.AQUA + "   " + currentSpell.getTrueDisplayName()));
						} else
							text.add(Component.literal(ChatFormatting.GRAY + "   " + currentSpell.getTrueDisplayName()));
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
		var capability = SpellHolderProvider.get(itemstack);
		this.wasHolding = true;
		Spell spell = SpellUtils.getSpell(itemstack);
		var spells = capability.map(ISpellHolder::getSpells).orElse(null);
		if (spells != null && !spells.isEmpty()) {
			player.startUsingItem(hand);
			addNbtToSpellItem(player);
			if (!world.isClientSide()) {
				if (isSpellBook) {
					world.playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 0.8F, 0.7F);
				}
				if (spell != SpellInit.EMPTY.get()) {
					if (spell.getSpellType() == SpellAttributes.Type.CAST) {
						switch (spell.getMarkerType()) {
							case COMBAT:
								world.playSound(null, player.blockPosition(), SoundInit.CHARGE_COMBAT.get(), SoundSource.PLAYERS, 0.9F, 0.7F);
								currentCastTime = 0;
							case UTILITY:
								world.playSound(null, player.blockPosition(), SoundInit.CHARGE_UTILITY.get(), SoundSource.PLAYERS, 0.9F, 0.8F);
								currentCastTime = 0;
						}
					}
				}
			}
			return InteractionResultHolder.success(itemstack);
		} else
			return InteractionResultHolder.pass(itemstack);
	}

	@Override
	public void onUseTick(Level pLevel, LivingEntity livingEntity, ItemStack stack, int pRemainingUseDuration) {

		if (livingEntity instanceof Player player /*&& (FMLEnvironment.dist.isDedicatedServer() || player.level().isClientSide)*/) {
			Spell spell = SpellUtils.getSpell(stack);
			ItemStack itemstack = player.getItemInHand(player.getUsedItemHand());

			if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == ItemInit.WIZARD_HAT.get()) {
				currentCastTime = currentCastTime + 2.4;
			} else if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() != ItemInit.WIZARD_HAT.get()) {
				currentCastTime = currentCastTime + 1.4;
			}


			if (spell instanceof PhasingII || spell instanceof EffectSpell<?> || spell instanceof DescentII || spell instanceof RespirationSpell || spell instanceof EmptySpell) {
			} else {
				try {
					if (spell instanceof HealingCircleSpell || spell instanceof LightningIII || spell instanceof FlamingCircleSpell || spell instanceof FreezingCircleSpell || spell instanceof SummonSpell<?> || spell instanceof FrostWallSpell || spell instanceof IlluminateII) {
						ResourceLocation fileLocation = ResourceLocation.parse(HLSpells.MODID + ":functions/large/large_rune_2.mcfunction");
						GenerateParticles.generateParticleRune(fileLocation, livingEntity, spell.getRune());
					} else {
						Random pRandom = new Random();
						for (BlockPos blockpos : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
							if (pRandom.nextInt(14) == 0) {
								livingEntity.level()
										.addParticle(ParticleTypes.ENCHANT,
												(double) livingEntity.getX(),
												(double) livingEntity.getY() + 2.0D,
												(double) livingEntity.getZ(),
												(double) ((float) blockpos.getX() + pRandom.nextFloat()) - 0.5D,
												(double) ((float) blockpos.getY() - pRandom.nextFloat() - 1.0F),
												(double) ((float) blockpos.getZ() + pRandom.nextFloat()) - 0.5D
										);
							}
						}
						ResourceLocation fileLocation = ResourceLocation.parse(HLSpells.MODID + ":functions/small/small_rune_2.mcfunction");
						GenerateParticles.generateParticleRune(fileLocation, livingEntity, spell.getRune());
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			var capability = SpellHolderProvider.get(itemstack);
			if (spell.getSpellType() == SpellAttributes.Type.HELD) {
				spell.execute(player, stack);

				capability.ifPresent(cap -> {
					if (cap.getSpellSoundBuffer() == 0) {
						if (spell instanceof Illuminate || spell instanceof IlluminateII) {
							player.level().playSound(null, player.blockPosition(), SoundInit.HELD_ILLUMINATE.get(), SoundSource.PLAYERS, 0.35F, 0.5F);
							cap.setSpellSoundBuffer(13);
						} else if (spell.getTrueDisplayName() == "Flaming Breath") {
								player.level().playSound(null, player.blockPosition(), SoundEvents.FIRE_AMBIENT, SoundSource.PLAYERS, 2.2F, 0.7F);
								cap.setSpellSoundBuffer(23);
							    player.level().playSound(null, player.blockPosition(), SoundEvents.BLAZE_AMBIENT, SoundSource.PLAYERS, 0.1F, 1.5F);
							    cap.setSpellSoundBuffer(30);
							    player.level().playSound(null, player.blockPosition(), SoundInit.HELD_COMBAT.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
								cap.setSpellSoundBuffer(23);
						} else if (spell.getTrueDisplayName() == "Freezing Breath") {
							player.level().playSound(null, player.blockPosition(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 0.16F, 0.7F);
							cap.setSpellSoundBuffer(23);
							player.level().playSound(null, player.blockPosition(), SoundEvents.BLAZE_AMBIENT, SoundSource.PLAYERS, 0.05F, 1.9F);
							cap.setSpellSoundBuffer(30);
							player.level().playSound(null, player.blockPosition(), SoundInit.HELD_COMBAT.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
							cap.setSpellSoundBuffer(23);
						}else if (spell.getTrueDisplayName() == "Wither Breath") {
							player.level().playSound(null, player.blockPosition(), SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 0.2F, 0.6F);
							cap.setSpellSoundBuffer(23);
							player.level().playSound(null, player.blockPosition(), SoundEvents.BLAZE_AMBIENT, SoundSource.PLAYERS, 0.03F, 1.6F);
							cap.setSpellSoundBuffer(30);
							player.level().playSound(null, player.blockPosition(), SoundInit.HELD_COMBAT.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
							cap.setSpellSoundBuffer(23);
						} else if (spell.getMarkerType() == SpellAttributes.Marker.COMBAT) {
							player.level().playSound(null, player.blockPosition(), SoundInit.HELD_COMBAT.get(), SoundSource.PLAYERS, 2.1F, 1.0F);
							cap.setSpellSoundBuffer(23);
						} else if (spell.getMarkerType() == SpellAttributes.Marker.UTILITY && spell.getTrueDisplayName() != "Phasing II") {
							player.level().playSound(null, player.blockPosition(), SoundInit.HELD_UTILITY.get(), SoundSource.PLAYERS, 1.8F, 1.3F);
							cap.setSpellSoundBuffer(23);
						}
					} else
						cap.setSpellSoundBuffer(cap.getSpellSoundBuffer() - 1);
				});
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		Spell spell = SpellUtils.getSpell(pContext.getItemInHand());
		if (spell instanceof RespawnSpell) {
			BlockPos blockpos = pContext.getClickedPos();
			Level level = pContext.getLevel();
			if (!level.getBlockState(blockpos).is(Blocks.LODESTONE)) {
				return super.useOn(pContext);
			} else {
				level.playSound(null, blockpos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
				Player player = pContext.getPlayer();
				ItemStack itemstack = pContext.getItemInHand();
				LodestoneTracker tracker = new LodestoneTracker(Optional.of(GlobalPos.of(level.dimension(), blockpos)), true);
				boolean flag = !player.getAbilities().instabuild && itemstack.getCount() == 1;
				if (flag) {
					itemstack.set(DataComponents.LODESTONE_TRACKER, tracker);
				} else {
					ItemStack copy = itemstack.copyWithCount(1);
					if (!player.getAbilities().instabuild) {
						itemstack.shrink(1);
					}
					copy.set(DataComponents.LODESTONE_TRACKER, tracker);
					if (!player.getInventory().add(copy)) {
						player.drop(copy, false);
					}
				}

				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return super.useOn(pContext);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int power) {
		if (entity instanceof Player player) {
			var capability = SpellHolderProvider.get(stack);
			capability.ifPresent(cap -> cap.setSpellSoundBuffer(0));
			this.wasHolding = false;

			Spell spell = SpellUtils.getSpell(stack);

			player.getCooldowns().addCooldown(stack.getItem(), 25);

			if (spell instanceof PhasingII) {
				player.setInvulnerable(false);
				player.setInvisible(false);
				player.setSilent(false);
			}

			if (spell instanceof Phasing) {
				player.setInvulnerable(false);
				player.setInvisible(false);
				player.setSilent(false);
			}

			if (this.castTimeCondition(player, stack)) {
				if (spell.getSpellType() == SpellAttributes.Type.CAST) {
					world.playSound(null, player.blockPosition(), spell.getSpellSound(), SoundSource.PLAYERS, 0.3F, 0.7F);
					spell.execute(player, stack);
				}

				if (!world.isClientSide()) {
					capability.filter(p -> !(p.getSpells().isEmpty())).ifPresent(cap -> {
						if (stack.getItem() instanceof StaffItem item) {
							if (item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.COMBAT) {
								player.getCooldowns().addCooldown(stack.getItem(), 30);
								currentCastTime = 0;
								stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
								//player.getItemInHand(player.getUsedItemHand()).hurt(1, 1 , null);

							} else if (!item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.UTILITY) {
								player.getCooldowns().addCooldown(stack.getItem(), 30);
								currentCastTime = 0;
								stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
							} else if (!item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.COMBAT) {
								player.getCooldowns().addCooldown(stack.getItem(), 10);
								currentCastTime = 0;
								stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
							} else if (item.isGemAmethyst() && SpellUtils.getSpellByID(cap.getCurrentSpell()).getMarkerType() == SpellAttributes.Marker.UTILITY) {
								player.getCooldowns().addCooldown(stack.getItem(), 10);
								currentCastTime = 0;
								stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
							}
						} else if (this.isSpellBook || !this.isSpellBook) {
							player.getCooldowns().addCooldown(stack.getItem(), 25);
							currentCastTime = 0;
							stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
						}

					});
				} else {
					Util.doParticles(player);
				}
			} else {
				player.playSound(SoundInit.MISCAST_SOUND.get(), 1.1f, 0.7f);
				currentCastTime = 0;
			}
			resetNbtOnSpellItem(player);
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		ItemEnchantments stored = book.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
		for (Holder<Enchantment> enchantment : stored.keySet()) {
			if (enchantment.is(Enchantments.MENDING) && EnchantmentInit.getLevel(Enchantments.MENDING, stack) >= 1
					|| enchantment.is(Enchantments.FIRE_ASPECT) && EnchantmentInit.getLevel(Enchantments.FIRE_ASPECT, stack) >= 1
					|| enchantment.is(Enchantments.SMITE) && EnchantmentInit.getLevel(Enchantments.SMITE, stack) >= 1
					|| enchantment.is(EnchantmentInit.SOUL_BOND)) {
				return !isSpellBook || stack.getItem() instanceof StaffItem;
			}
			if (enchantment.is(Enchantments.UNBREAKING)
					&& EnchantmentInit.getLevel(Enchantments.UNBREAKING, stack) <= stored.getLevel(enchantment)
					&& EnchantmentInit.getLevel(Enchantments.UNBREAKING, stack) != 3) {
				return !isSpellBook || stack.getItem() instanceof StaffItem;
			}
		}
		return false;
	}

	@Override
	public boolean isFoil(ItemStack pStack) {
		var spells = SpellHolderProvider.get(pStack).map(ISpellHolder::getSpells).orElse(null);
		return isSpellBook && SpellUtils.getSpell(pStack) != SpellInit.EMPTY.get() || !isSpellBook && spells != null && !spells.isEmpty() || super.isFoil(pStack);
	}

	@Override
	@NotNull
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return NONE;
	}

	@Override
	protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
		projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0F, velocity, inaccuracy);
	}

	public static final Predicate<ItemStack> NONE = (p_43018_) -> p_43018_.is(Items.EXPERIENCE_BOTTLE);

	@Override
	@NotNull
	public UseAnim getUseAnimation(@NotNull ItemStack pStack) {
		return isSpellBook ? UseAnim.CROSSBOW : UseAnim.BLOCK;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 8;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack pStack, @NotNull LivingEntity entity) {
		return 72000;
	}


	@Override
	public boolean isEnchantable(@NotNull ItemStack pStack) {
		return isSpellBook;
	}

	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		return false;
	}

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

	private static boolean isCasting(ItemStack stack) {
		return stack.getOrDefault(SpellHolderProvider.CAST_BAR.get(), false);
	}

	private static final int BAR_COLOR = Mth.color(0.4F, 1.0F, 0.8F);

	@Override
	public int getBarColor(ItemStack pStack) {
		if (isCasting(pStack)) {
			return BAR_COLOR;
		}
		else {
			float stackMaxDamage = (float)this.getMaxDamage(pStack);
			float f = Math.max(0.0F, (stackMaxDamage - (float)pStack.getDamageValue()) / stackMaxDamage);
			return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
		}
	}

	public boolean isBarVisible(ItemStack pStack) {
		if (isCasting(pStack) || pStack.isDamaged()) {
			return true;
		} else {
			return false;
		}
	}



	public int getBarWidth(ItemStack pStack) {
		if (pStack.getItem() instanceof StaffItem item && isCasting(pStack)) {
			return (int) Math.min(currentCastTime * item.getCastDelay() / 20, 13);

		}
//		else if (!this.isSpellBook && isCasting(pStack)) {
//			return (int) Math.min(currentCastTime * 0.42 * 20 / 25, 13);
//		}
		else if (isCasting(pStack)) {
			return (int) Math.min(currentCastTime * 0.28 * 20 / 20, 13);
		}
		return Math.round(13.0F - (float)pStack.getDamageValue() * 13.0F / (float)this.getMaxDamage(pStack));
	}

	private void addNbtToSpellItem(Player player) {
		ItemStack spellItem = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (spellItem.getItem() instanceof SpellHoldingItem) {
			spellItem.set(SpellHolderProvider.CAST_BAR.get(), true);
		}
	}

	private void resetNbtOnSpellItem(Player player) {
		ItemStack spellItem = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (spellItem.getItem() instanceof SpellHoldingItem) {
			spellItem.set(SpellHolderProvider.CAST_BAR.get(), false);
		}
	}
	
	private boolean castTimeCondition(Player player, ItemStack stack) {
		if (stack.getItem() instanceof StaffItem item) {
			return currentCastTime >= (item.getMaxCastTime() + 0.1);
		}
//		else if (!this.isSpellBook) {
//			return currentCastTime >= (45 + 13);
//		}
		else {
			return currentCastTime >= (50 + 0.2);
		}
		
	}
}