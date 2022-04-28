package com.divinity.hlspells.setup.init;


import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.SinkingCurse;
import com.divinity.hlspells.enchantments.SoulBond;
import com.divinity.hlspells.enchantments.SoulSyphon;
import com.divinity.hlspells.enchantments.spells.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HLSpells.MODID);

    public static final RegistryObject<Enchantment> SOUL_BOND = ENCHANTMENTS.register("soul_bond", () -> new SoulBond(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SOUL_SYPHON = ENCHANTMENTS.register("soul_syphon", () -> new SoulSyphon(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ABSORBING = ENCHANTMENTS.register("absorbing", () -> new Absorbing(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> AQUA_BOLT = ENCHANTMENTS.register("aqua_bolt", () -> new AquaBolt(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ARROW_RAIN = ENCHANTMENTS.register("arrow_rain", () -> new ArrowRain(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BLAST = ENCHANTMENTS.register("blast", () -> new Blast(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BOLT = ENCHANTMENTS.register("bolt", () -> new Bolt(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BOND = ENCHANTMENTS.register("bond", () -> new Bond(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FANGS = ENCHANTMENTS.register("fangs", () -> new Fangs(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FIRE_BALL = ENCHANTMENTS.register("fire_ball", () -> new FireBall(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_BOLT = ENCHANTMENTS.register("flaming_bolt", () -> new FlamingBolt(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_CIRCLE = ENCHANTMENTS.register("flaming_circle", () -> new FlamingCircle(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> HEALING_CIRCLE = ENCHANTMENTS.register("healing_circle", () -> new HealingCircle(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> KNOCKBACK_BOLT = ENCHANTMENTS.register("knockback_bolt", () -> new KnockbackBolt(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LEVITATION = ENCHANTMENTS.register("levitation", () -> new Levitation(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LIGHTNING_BOLT = ENCHANTMENTS.register("lightning_bolt", () -> new LightningBolt(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LURE = ENCHANTMENTS.register("lure", () -> new Lure(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING_BOLT = ENCHANTMENTS.register("piercing_bolt", () -> new PiercingBolt(EquipmentSlot.MAINHAND));
    //public static final RegistryObject<Enchantment> PROTECTION_CIRCLE = ENCHANTMENTS.register("protection_circle", () -> new ProtectionCircle(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PULL = ENCHANTMENTS.register("pull", () -> new Pull(EquipmentSlot.MAINHAND));
    //public static final RegistryObject<Enchantment> REPEL = ENCHANTMENTS.register("repel", () -> new Repel(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS.register("respiration", () -> new Respiration(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FEATHER_FALLING = ENCHANTMENTS.register("slow_fall", () -> new SlowFall(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SOUL_SUMMON = ENCHANTMENTS.register("soul_summon", () -> new SoulSummon(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SPEED = ENCHANTMENTS.register("speed", () -> new Speed(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> STORM = ENCHANTMENTS.register("lightning_chain", () -> new LightningChain(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> TELEPORT = ENCHANTMENTS.register("teleport", () -> new Teleport(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FROST_PATH = ENCHANTMENTS.register("frost_path",  () -> new FrostPath(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> CURSE_OF_SINKING = ENCHANTMENTS.register("curse_of_sinking", () -> new SinkingCurse(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS,  EquipmentSlot.FEET));
    public static final RegistryObject<Enchantment> WITHER_SKULL = ENCHANTMENTS.register("wither_skull", () -> new WitherSkull(EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> TORPEDO = ENCHANTMENTS.register("torpedo", () -> new Torpedo(EquipmentSlot.MAINHAND));
}