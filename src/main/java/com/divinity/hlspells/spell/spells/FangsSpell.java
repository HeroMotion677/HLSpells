package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.projectile.InvisibleTargetingEntity;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.entity.Entity.RemovalReason;

public class FangsSpell extends Spell {

    public FangsSpell(SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, RegistryObject<SimpleParticleType> rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            float f = (float) Mth.atan2(p.getZ(), p.getX());
            if (!p.isShiftKeyDown()) {
                InvisibleTargetingEntity stormBullet = new InvisibleTargetingEntity(EntityInit.INVISIBLE_TARGETING_ENTITY.get(), p.level) {
                    @Override
                    public void tick() {
                        super.tick();
                        if (this.getInitialPosition() != null) {
                            float distance = Mth.sqrt((float) distanceToSqr(this.getInitialPosition()));
                            if (distance >= 10) {
                                this.remove(RemovalReason.DISCARDED);
                            }
                            if (this.level.getGameTime() % 2 == 0) {
                                if (this.getOwner() instanceof Player) {
                                    createFangsEntity((LivingEntity) this.getOwner(), this.level, this.xOld, this.zOld, getY(), 0, 0);
                                }
                            }
                        }
                    }
                };
                stormBullet.setOwner(p);
                stormBullet.setInitialPosition(p.position());
                stormBullet.setPos(p.getX(), p.getY(), p.getZ());
                stormBullet.shootFromRotation(p, p.xRot, p.yRot, 1.2F, 1.2F, 1.2F);
                stormBullet.setDeltaMovement(Mth.cos((float) Math.toRadians(p.yRot + 90)), 0, Mth.sin((float) Math.toRadians(p.yRot + 90)));
                p.level.addFreshEntity(stormBullet);
            }
            else {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + i * (float) Math.PI * 0.4F;
                    createFangsEntity(p, p.level, p.getX() + Mth.cos(f1) * 1.5D, p.getZ() + Mth.sin(f1) * 1.5D, p.getY(), f1, 0);
                }
                for (int k = 0; k < 8; ++k) {
                    float f2 = f + k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
                    createFangsEntity(p, p.level, p.getX() + Mth.cos(f2) * 2.5D, p.getZ() + Mth.sin(f2) * 2.5D, p.getY(), f2, 3);
                }
            }
            return true;
        };
    }

    public static void createFangsEntity(LivingEntity entity, Level world, double x, double z, double y, float yaw, int warmup) {
        BlockPos blockpos = new BlockPos(x, y, z);
        boolean flag = false;
        double d0 = 0.0D;
        do {
            BlockPos below = blockpos.below();
            BlockState blockstate = world.getBlockState(below);
            if (blockstate.isFaceSturdy(world, below, Direction.UP)) {
                if (!world.isEmptyBlock(blockpos)) {
                    BlockState state = world.getBlockState(blockpos);
                    VoxelShape voxelshape = state.getCollisionShape(world, blockpos);
                    if (!voxelshape.isEmpty()) d0 = voxelshape.max(Direction.Axis.Y);
                }
                flag = true;
                break;
            }
            blockpos = blockpos.below();
        } while (blockpos.getY() >= Mth.floor(y + 1) - 1);
        if (flag) world.addFreshEntity(new EvokerFangs(world, x, blockpos.getY() + d0, z, yaw, warmup, entity));
    }
}
