package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(new ResourceLocation("spell"), HLSpells.MODID);

    public static final RegistryObject<Spell> EMPTY = register("no_spell", () -> new EmptySpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "No Spell", 0, false, 1));
    public static final RegistryObject<Spell> DESCENT = register("descent", () -> new EffectSpell<>(MobEffects.SLOW_FALLING, SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Descent", 1, 15, false, 1));
    public static final RegistryObject<Spell> BLAST = register("blast", () -> new BlastSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Blast", 6, false, 1));
    public static final RegistryObject<Spell> TELEPORT = register("teleport", () -> new TeleportSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Teleport", 9, false, 1));
    public static final RegistryObject<Spell> ARROW_RAIN = register("arrow_rain", () -> new ArrowRainSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Arrow Rain", 1, 6, false, 1));
    public static final RegistryObject<Spell> BOLT = register("bolt", () -> new BoltSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Bolt", 5, false, 1));
    public static final RegistryObject<Spell> HEALING_CIRCLE = register("healing_circle", () -> new HealingCircleSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Healing Circle", 1, 3, false, 1));
    public static final RegistryObject<Spell> SOUL_SUMMON = register("soul_summon", () -> new SummonSpell<>(EntityInit.SUMMONED_VEX_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Soul Summon", 16, true, 1));
    public static final RegistryObject<Spell> PULL = register("pull", () -> new PullSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Pull", 3, false, 1));
    public static final RegistryObject<Spell> BOND = register("bond", () -> new BondSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Bond", 1, false, 1));
    public static final RegistryObject<Spell> LIGHTNING_CHAIN = register("lightning_chain", () -> new LightningChainSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Lightning Chain", 14, false, 1));
    public static final RegistryObject<Spell> FIRE_BALL = register("fire_ball", () -> new ProjectileSpell<>(EntityType.FIREBALL, SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Fire Ball", 5, false, 1).viewVectorOffset(1.5D).yPosOffset(0.5D));
    public static final RegistryObject<Spell> LEVITATION = register("levitation", () -> new EffectSpell<>(MobEffects.LEVITATION, SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Levitation", 1, 6, false, 1));
    public static final RegistryObject<Spell> LIGHTNING = register("lightning", () -> new LightningBoltSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Lightning", 8, false, 1));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = register("knockback_bolt", () -> new ProjectileSpell<>(EntityInit.KNOCKBACK_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Knockback Bolt", 3, false, 1));
    public static final RegistryObject<Spell> SPEED = register("speed", () -> new SpeedSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Speed", 1, 1, false, 1));
    public static final RegistryObject<Spell> PIERCING_BOLT = register("piercing_bolt", () -> new ProjectileSpell<>(EntityInit.PIERCING_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, "Piercing Bolt", 12, false, 1).yPosOffset(1.35D));
    public static final RegistryObject<Spell> ABSORBING = register("absorbing", () -> new AbsorbingSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Absorbing", 6, false, 1));
    public static final RegistryObject<Spell> FANGS = register("fangs", () -> new FangsSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Fangs", 6, true, 1));
    public static final RegistryObject<Spell> FLAMING_BOLT = register("flaming_bolt", () -> new ProjectileSpell<>(EntityInit.FLAMING_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, "Flaming Bolt", 7, false, 1).yPosOffset(1.35D));
    public static final RegistryObject<Spell> AQUA_BOLT = register("aqua_bolt", () -> new ProjectileSpell<>(EntityInit.AQUA_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Aqua Bolt", 4, false, 1).yPosOffset(1.35D));
    public static final RegistryObject<Spell> LURE = register("lure", () -> new LureSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Lure", 1, 10, false, 1));
    public static final RegistryObject<Spell> FROST_PATH =  register("frost_path", () -> new FrostPathSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Frost Path", 1, 6, false, 1));
    public static final RegistryObject<Spell> WITHER_SKULL = register("wither_skull", () -> new ProjectileSpell<>(EntityType.WITHER_SKULL, SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Wither Skull", 10, false, 1).viewVectorOffset(1.5D).yPosOffset(1D));
    public static final RegistryObject<Spell> TORPEDO = register("torpedo", () -> new TorpedoSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Torpedo", 8, false, 1));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new FlamingCircleSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Flaming Circle", 1, 6, false, 1));
    public static final RegistryObject<Spell> RESPIRATION = register("respiration", () -> new RespirationSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Respiration", 1, 15, false, 1));
    public static final RegistryObject<Spell> FREEZING_BOLT = register("freezing_bolt", () -> new ProjectileSpell<>(EntityInit.FREEZING_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Freezing Bolt", 7, false, 1).yPosOffset(1.35D));
    public static final RegistryObject<Spell> CHORUS_BOLT = register("chorus_bolt",() -> new ProjectileSpell<>(EntityInit.CHORUS_BOLT_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Chorus Bolt", 7, false, 1).yPosOffset(1.35D));
    public static final RegistryObject<Spell> PHASING = register("phasing", () -> new PhasingSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Phasing", 1, 6, false, 1));
    public static final RegistryObject<Spell> NECROMANCY = register("necromancy", () -> new SummonSpell<>(EntityInit.SUMMONED_SKELETON_ENTITY.orElse(null), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Necromancy", 16, false, 1));

    // Optional Spells
    public static final RegistryObject<Spell> ILLUMINATE = initializeOptionalSpell("illuminate", "lucent", () -> new IlluminateSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Illuminate", 10, 6, false, 2));

    //public static final RegistryObject<Spell> REPEL = register("repel", () -> new Spell(SpellAttributes.HELD, SpellActions::doRepel, "Repel", 1, 3));
    //public static final RegistryObject<Spell> PROTECTION_CIRCLE = register("protection_circle", () -> new Spell(SpellAttributes.HELD, SpellActions::doProtectionCircle, "Protection Circle", 1, 6));

    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS.makeRegistry(Spell.class,
            () -> new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1)
                    .onAdd((owner, stage, id, obj, oldObj) -> {})
                    .setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty")));

    private static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        RegistryObject<Spell> registryObject = SPELLS.register(name, spell);
        HLSpells.LOGGER.info("Spell added : " + spell.get().getTrueDisplayName() + " ");
        registerSpellLevels(spell.get());
        return registryObject;
    }

    @Nullable
    private static RegistryObject<Spell> initializeOptionalSpell(String spellName, String requiredModId, Supplier<Spell> spellSupplier) {
        if (ModList.get().isLoaded(requiredModId)) {
            return register(spellName, spellSupplier);
        }
        return null;
    }

    private static void registerSpellLevels(Spell spell) {
        if (spell.getMaxSpellLevel() > 1) {
            for (int i = 2; i <= spell.getMaxSpellLevel(); i++) {
                int finalI = i;
                Spell finalSpell = spell.clone();
                if (finalSpell != null) {
                    String registryString = "";
                    char[] c = finalSpell.getTrueDisplayName().toCharArray();
                    c[0] = Character.toLowerCase(c[0]);
                    registryString = new String(c);
                    SPELLS.register(registryString + "_" + numberToRomanNumeral(finalI).toLowerCase(),
                            () -> finalSpell.setSpellLevel(finalI).setTrueDisplayName(spell.getTrueDisplayName() + " " + numberToRomanNumeral(finalI)));
                    HLSpells.LOGGER.info("Level added for %s: %s".formatted(finalSpell.getTrueDisplayName(), numberToRomanNumeral(finalI)) + " ");
                }
            }
        }
    }

    private static String numberToRomanNumeral(int number) {
        return "I".repeat(number);
    }
}
