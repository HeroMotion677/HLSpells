package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellAttributes;
import com.divinity.hlspells.spell.spells.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = HLSpells.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SpellInit {

    public static final ResourceKey<Registry<Spell>> SPELL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(HLSpells.MODID, "spell"));

    public static final DeferredRegister<Spell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, HLSpells.MODID);

    public static final Registry<Spell> SPELLS_REGISTRY = SPELLS.makeRegistry(builder -> {});

    public static final DeferredHolder<Spell, Spell> EMPTY = register("no_spell", () -> new EmptySpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "No Spell", 0, true, 1));
    public static final DeferredHolder<Spell, Spell> DESCENT = register("descent", () -> new EffectSpell<>(MobEffects.SLOW_FALLING, SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Descent", 2, 15, false, 1));
    public static final DeferredHolder<Spell, Spell> DESCENT_II = register("descent_ii", () -> new DescentII(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Descent II", 3, 15, true, 1));
    public static final DeferredHolder<Spell, Spell> TELEPORT = register("teleport", () -> new TeleportSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Teleport", 8, false, 1, orNull(ParticlesInit.PURPLE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> ARROW_RAIN = register("arrow_rain", () -> new ArrowRainSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Arrow Rain", 1, 6, false, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> SOUL_SUMMON = register("soul_summon", () -> new SummonSpell<>(orNull(EntityInit.SUMMONED_VEX_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Vex Summon", 20, true, 1, orNull(ParticlesInit.BLACK_PARTICLE)).summonCount(4));
    public static final DeferredHolder<Spell, Spell> FANGS = register("fangs", () -> new FangsSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Fangs", 6, true, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> BOND = register("bond", () -> new BondSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Bond", 1, false, 1, orNull(ParticlesInit.GREEN_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> LIGHTNING = register("lightning", () -> new Lightning(SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Lightning", 4, false, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> LIGHTNING_II = register("lightning_ii", () -> new LightningII(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Lightning II", 6, true, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> LIGHTNING_III = register("lightning_iii", () -> new LightningIII(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Lightning III", 15, true, 1, orNull(ParticlesInit.BLUE_PARTICLE)));
    public static final DeferredHolder<Spell, Spell> FIRE_BALL = register("fire_ball", () -> new ProjectileSpell<>(orNull(EntityInit.FIREBALL), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Fire Ball", 8, false, 1, 1, orNull(ParticlesInit.ORANGE_PARTICLE_SMALL)).viewVectorOffset(0D).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> FIRE_BALL_II = register("fire_ball_ii", () -> new ProjectileSpell<>(orNull(EntityInit.FIREBALL2), SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Fire Ball II", 15, true, 1, 1, orNull(ParticlesInit.PURPLE_PARTICLE_SMALL)).viewVectorOffset(0D).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> LEVITATION = register("levitation", () -> new EffectSpell<>(MobEffects.LEVITATION, SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Levitation", 1, 6, false, 1));
    public static final DeferredHolder<Spell, Spell> SPEED = register("speed", () -> new SpeedSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Speed", 1, 1, false, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    //public static final DeferredHolder<Spell, Spell> FORTIFY = register("fortify", () -> new Fortify(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Fortify", 1, 1, false, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> ABSORBING = register("absorbing", () -> new AbsorbingSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Absorbing", 1, false, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    //public static final DeferredHolder<Spell, Spell> BOLT = register("bolt", () -> new Bolt(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Bolt", 4, false, 1, orNull(ParticlesInit.GREEN_PARTICLE_SMALL)));
    //public static final DeferredHolder<Spell, Spell> BOLT_II = register("bolt_ii", () -> new BoltII(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Bolt II", 4, true, 1, orNull(ParticlesInit.GREEN_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> FLAMING_BOLT = register("flaming_bolt", () -> new ProjectileSpell<>(orNull(EntityInit.FLAMING_BOLT_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, "Flaming Bolt", 4, false, 1, 1, orNull(ParticlesInit.ORANGE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> AQUA_BOLT = register("aqua_bolt", () -> new ProjectileSpell<>(orNull(EntityInit.AQUA_BOLT_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Aqua Bolt", 4, false, 1, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> FREEZING_BOLT = register("freezing_bolt", () -> new ProjectileSpell<>(orNull(EntityInit.FREEZING_BOLT_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, "Freezing Bolt", 4, false, 1, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> PIERCING_BOLT = register("piercing_bolt", () -> new ProjectileSpell<>(orNull(EntityInit.PIERCING_BOLT_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Bolt", 6, false, 1, 1, orNull(ParticlesInit.GREEN_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> PIERCING_BOLT_II = register("piercing_bolt_ii", () -> new PiercingBoltIISpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Bolt II", 6, true, 1, orNull(ParticlesInit.GREEN_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> CHORUS_BOLT = register("chorus_bolt",() -> new ProjectileSpell<>(orNull(EntityInit.CHORUS_BOLT_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Chorus Bolt", 4, false, 1, 1, orNull(ParticlesInit.PURPLE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> FROST_PATH = register("frost_path", () -> new FrostPath(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Frost Path", 1,  4, false, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> FROST_PATH_II =  register("frost_path_ii", () -> new FrostPathII(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Frost Path II", 4, 6, true, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> FROST_WALL = register("frost_wall", () -> new FrostWallSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Frost Wall", 1, 6, false, 1, orNull(ParticlesInit.BLUE_PARTICLE)));
    public static final DeferredHolder<Spell, Spell> WITHER_SKULL = register("wither_skull", () -> new ProjectileSpell<>(orNull(EntityInit.WITHER_SKULL_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Wither Skull", 6, false, 1, 1, orNull(ParticlesInit.BLACK_PARTICLE_SMALL)).viewVectorOffset(0D).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> TORPEDO = register("torpedo", () -> new TorpedoSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Torpedo", 8, false, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> HEALING_CIRCLE = register("healing_circle", () -> new HealingCircleSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Healing Circle", 2, 3, false, 1, orNull(ParticlesInit.RED_PARTICLE)));
    public static final DeferredHolder<Spell, Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new FlamingCircleSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.COMBAT, "Flaming Circle", 4, 6, false, 1, ParticleTypes.SMALL_FLAME));
    public static final DeferredHolder<Spell, Spell> FREEZING_CIRCLE = register("freezing_circle", () -> new FreezingCircleSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.COMBAT, "Freezing Circle", 4, 4, false, 1, ParticleTypes.SNOWFLAKE));
    public static final DeferredHolder<Spell, Spell> FLAMING_BREATH = register("flaming_breath", () -> new BreathSpell<>(orNull(EntityInit.FLAMING_BREATH_ENTITY), SpellAttributes.Type.HELD, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Flaming Breath", 3, false, 7, 1, orNull(ParticlesInit.ORANGE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> FREEZING_BREATH = register("freezing_breath", () -> new BreathSpell<>(orNull(EntityInit.FREEZING_BREATH_ENTITY), SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Freezing Breath", 3, false, 7, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> WITHER_BREATH = register("wither_breath", () -> new BreathSpell<>(orNull(EntityInit.WITHER_BREATH_ENTITY), SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Wither Breath", 3, false, 7, 1, orNull(ParticlesInit.BLACK_PARTICLE_SMALL)).yPosOffset(0D));
    public static final DeferredHolder<Spell, Spell> RESPIRATION = register("respiration", () -> new RespirationSpell(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.ONE, SpellAttributes.Marker.UTILITY, "Respiration", 1, 15, false, 1));
    public static final DeferredHolder<Spell, Spell> PHASING = register("phasing", () -> new Phasing(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Phasing", 1, 6, false, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> PHASING_II = register("phasing_ii", () -> new PhasingII(SpellAttributes.Type.HELD, SpellAttributes.Rarity.COMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.UTILITY, "Phasing II", 4, 6, false, 1));
    public static final DeferredHolder<Spell, Spell> NECROMANCY = register("necromancy", () -> new SummonSpell<>(orNull(EntityInit.SUMMONED_WITHER_SKELETON_ENTITY), SpellAttributes.Type.CAST, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Necromancy", 30, false, 1, orNull(ParticlesInit.BLACK_PARTICLE)).summonCount(4));
    public static final DeferredHolder<Spell, Spell> RESPAWN = register("respawn", () -> new RespawnSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Recall", 20, false, 1, orNull(ParticlesInit.WHITE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> SONIC_BOOM = register("sonic_boom", () -> new SonicBoomSpell(SpellAttributes.Type.CAST, SpellAttributes.Rarity.RARE, SpellAttributes.Tier.THREE, SpellAttributes.Marker.COMBAT, "Sonic Boom", 17, true, 1, orNull(ParticlesInit.BLUE_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> ILLUMINATE = register("illuminate", () -> new Illuminate(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Illuminate", 1, 6, false, 1, orNull(ParticlesInit.YELLOW_PARTICLE_SMALL)));
    public static final DeferredHolder<Spell, Spell> ILLUMINATE_II = register("illuminate_ii", () -> new IlluminateII(SpellAttributes.Type.HELD, SpellAttributes.Rarity.UNCOMMON, SpellAttributes.Tier.TWO, SpellAttributes.Marker.UTILITY, "Illuminate II", 2, 6, true, 1, orNull(ParticlesInit.YELLOW_PARTICLE)));

    private static <R, T extends R> T orNull(DeferredHolder<R, T> holder) {
        return holder.isBound() ? holder.get() : null;
    }

    private static DeferredHolder<Spell, Spell> register(String name, Supplier<Spell> spell) {
        DeferredHolder<Spell, Spell> registryObject = SPELLS.register(name, spell);
        HLSpells.LOGGER.info("Spell added : " + spell.get().getTrueDisplayName() + " ");
        registerSpellLevels(spell.get());
        return registryObject;
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

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            for(Spell spell : SPELLS_REGISTRY){
                if(spell.isEmpty()){
                    continue;
                }
                event.accept(createSpellBookFor(spell));
            }
        }

    }

    public static ItemStack createSpellBookFor(Spell spell) {
        ItemStack stack = new ItemStack(ItemInit.SPELL_BOOK.get());
        SpellHolderProvider.get(stack).ifPresent(cap -> {
            ResourceLocation id = SpellInit.SPELLS_REGISTRY.getKey(spell);
            if (id != null) {
                cap.addSpell(Objects.requireNonNull(SpellInit.SPELLS_REGISTRY.getKey(spell)).toString());
            }
        });
        return stack;
    }
}
