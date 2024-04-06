package com.divinity.hlspells.spell.spells;

import com.divinity.hlspells.entities.Summonable;
import com.divinity.hlspells.entities.living.summoned.SummonedVexEntity;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.SpellConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummonSpell<T extends Entity & Summonable> extends Spell {

    private final EntityType<T> summoned;
    private int summonCount;
    private final List<Item> items;

    // Generic attribute increase factor per spell level
    private double attributeIncrease;
    private final Map<Attribute, Double> attributeMap;

    public SummonSpell(EntityType<T> summoned, SpellAttributes.Type type, SpellAttributes.Rarity rarity, SpellAttributes.Tier tier, SpellAttributes.Marker marker, String displayName, int xpCost, boolean treasureOnly, int maxSpellLevel, SimpleParticleType rune) {
        super(type, rarity, tier, marker, displayName, xpCost, treasureOnly, maxSpellLevel, rune);
        this.summoned = summoned;
        this.summonCount = 1;
        this.items = new ArrayList<>();
        this.attributeIncrease = 0;
        this.attributeMap = new HashMap<>();
    }

    @Override
    public SpellConsumer<Player> getAction() {
        return p -> {
            for (int i = 0; i < this.summonCount; i++) {
                Entity summoned = this.summoned.create(p.level);
                if (summoned instanceof Mob mob && summoned instanceof Summonable summonable) {
                    BlockPos blockPos = p.blockPosition().offset(-2 + p.level.random.nextInt(5), 0, -2 + p.level.random.nextInt(5));
                    mob.moveTo(blockPos, 0.0F, 0.0F);
                    if(mob instanceof SummonedVexEntity vex){
                        vex.setBoundOrigin(blockPos);
                    }
                    summonable.setSummonedOwner(p);
                    if (p.level instanceof ServerLevel level) {
                        this.items.stream().filter(item -> item instanceof ArmorItem || item instanceof TieredItem).forEach(item -> mob.setItemSlot(this.getSlotForItem(new ItemStack(item)), new ItemStack(item)));
                        this.doAttributeModification(mob);
                        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                        level.addFreshEntityWithPassengers(mob);
                    }
                    else return false;
                }
                else return false;
            }
            return true;
        };
    }

    @Override
    public SoundEvent getSpellSound() {
        return SoundInit.CAST_NECROMANCY.get();
    }

    public SummonSpell<T> summonCount(int summonCount) {
        this.summonCount = summonCount;
        return this;
    }

    public SummonSpell<T> addEquipment(Item... items) {
        this.items.addAll(List.of(items));
        return this;
    }

    /**
     * @param attributeIncrease Increases all the attributes on the summons by this factor per level
     */
    public SummonSpell<T> attributeLevelIncreaseFactor(double attributeIncrease) {
        this.attributeIncrease = attributeIncrease;
        return this;
    }

    /**
     * @param attributeMap Takes in a map of Attribute (A) -> Double (D) to modify X amount of attributes on the summons by D factor per level
     */
    public SummonSpell<T> attributeLevelIncreaseFactorSpecific(Map<Attribute, Double> attributeMap) {
        this.attributeMap.putAll(attributeMap);
        return this;
    }

    private EquipmentSlot getSlotForItem(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem() instanceof ArmorItem item) {
                return item.getSlot();
            }
            else if (stack.getItem() instanceof TieredItem) {
                return EquipmentSlot.MAINHAND;
            }
        }
        return EquipmentSlot.OFFHAND;
    }

    private void doAttributeModification(Mob mob) {
        if (this.attributeIncrease != 0) {
            mob.getAttributes().attributes.values().forEach(instance -> instance.setBaseValue(instance.getBaseValue() + (this.attributeIncrease * this.getSpellLevel())));
        }
        else if (!this.attributeMap.isEmpty()) {
            this.attributeMap.forEach((k, v) -> {
                AttributeInstance instance = mob.getAttribute(k);
                if (instance != null) {
                    instance.setBaseValue(instance.getBaseValue() + (v * this.getSpellLevel()));
                }
            });
        }
    }
}
