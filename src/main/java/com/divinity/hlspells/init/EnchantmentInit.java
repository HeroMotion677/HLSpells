package com.divinity.hlspells.init;


import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.enchantments.SoulBond;
import com.divinity.hlspells.enchantments.SoulSyphon;
import com.divinity.hlspells.enchantments.spells.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HLSpells.MODID);

    public static final RegistryObject<Enchantment> SOUL_BOND = ENCHANTMENTS.register("soul_bond", () -> new SoulBond(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SOUL_SYPHON = ENCHANTMENTS.register("soul_syphon", () -> new SoulSyphon(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ABSORBING = ENCHANTMENTS.register("absorbing", () -> new Absorbing(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> AQUA_BOLT = ENCHANTMENTS.register("aqua_bolt", () -> new AquaBolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> ARROW_RAIN = ENCHANTMENTS.register("arrow_rain", () -> new ArrowRain(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BLAST = ENCHANTMENTS.register("blast", () -> new Blast(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BOLT = ENCHANTMENTS.register("bolt", () -> new Bolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> BOND = ENCHANTMENTS.register("bond", () -> new Bond(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FANGS = ENCHANTMENTS.register("fangs", () -> new Fangs(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FIRE_BALL = ENCHANTMENTS.register("fire_ball", () -> new FireBall(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_BOLT = ENCHANTMENTS.register("flaming_bolt", () -> new FlamingBolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FLAMING_CIRCLE = ENCHANTMENTS.register("flaming_circle", () -> new FlamingCircle(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> HEALING_CIRCLE = ENCHANTMENTS.register("healing_circle", () -> new HealingCircle(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> KNOCKBACK_BOLT = ENCHANTMENTS.register("knockback_bolt", () -> new KnockbackBolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> LEVITATION = ENCHANTMENTS.register("levitation", () -> new Levitation(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> LIGHTNING_BOLT = ENCHANTMENTS.register("lightning_bolt", () -> new LightningBolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> LURE = ENCHANTMENTS.register("lure", () -> new Lure(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PIERCING_BOLT = ENCHANTMENTS.register("piercing_bolt", () -> new PiercingBolt(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PROTECTION_CIRCLE = ENCHANTMENTS.register("protection_circle", () -> new ProtectionCircle(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> PULL = ENCHANTMENTS.register("pull", () -> new Pull(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> REPEL = ENCHANTMENTS.register("repel", () -> new Repel(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> RESPIRATION = ENCHANTMENTS.register("respiration", () -> new Respiration(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> FEATHER_FALLING = ENCHANTMENTS.register("feather_falling", () -> new SlowFall(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SOUL_SUMMON = ENCHANTMENTS.register("soul_summon", () -> new SoulSummon(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> SPEED = ENCHANTMENTS.register("speed", () -> new Speed(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> STORM = ENCHANTMENTS.register("storm", () -> new Storm(EquipmentSlotType.MAINHAND));
    public static final RegistryObject<Enchantment> TELEPORT = ENCHANTMENTS.register("teleport", () -> new Teleport(EquipmentSlotType.MAINHAND));

}