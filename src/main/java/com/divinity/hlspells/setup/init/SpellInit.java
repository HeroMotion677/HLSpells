package com.divinity.hlspells.setup.init;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.spell.Spell;
import com.divinity.hlspells.spell.SpellTypes;
import com.divinity.hlspells.spells.SpellActions;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.Objects;
import java.util.function.Supplier;

public class SpellInit {

    public static final DeferredRegister<Spell> SPELLS_DEFERRED_REGISTER = DeferredRegister.create(new ResourceLocation("spell"), HLSpells.MODID);

    public static final RegistryObject<Spell> EMPTY = register("empty", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.NONE, (p, w) -> false, "No Spell"));
    public static final RegistryObject<Spell> SLOW_FALL = register("slow_fall", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doSlowFall, "Slow Falling", 1, 15, false));
    public static final RegistryObject<Spell> BLAST = register("blast", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doBlastSpell, "Blast", 6, false));
    public static final RegistryObject<Spell> TELEPORT = register("teleport", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doTeleport, "Teleport", 9, false));
    public static final RegistryObject<Spell> ARROW_RAIN = register("arrow_rain", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doArrowRain, "Arrow Rain", 1, 6, false));
    public static final RegistryObject<Spell> BOLT = register("bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doBoltSpell, "Bolt", 5, false));
    public static final RegistryObject<Spell> HEALING_CIRCLE = register("healing_circle", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doHealingCircle, "Healing Circle", 1, 3, false));
    public static final RegistryObject<Spell> SOUL_SUMMON = register("soul_summon", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doSummonSpell, "Soul Summon", 16, true));
    public static final RegistryObject<Spell> PULL = register("pull", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doPullSpell, "Pull", 3, false));
    public static final RegistryObject<Spell> BOND = register("bond", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doBondSpell, "Bond", 1, false));
    public static final RegistryObject<Spell> LIGHTNING_CHAIN = register("lightning_chain", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doLightingChain, "Lightning Chain", 14, false));
    public static final RegistryObject<Spell> FIRE_BALL = register("fire_ball", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doFireBallSpell, "Fire Ball", 5, false));
    public static final RegistryObject<Spell> LEVITATION = register("levitation", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doLevitation, "Levitation", 1, 6, false));
    public static final RegistryObject<Spell> LIGHTNING_BOLT = register("lightning_bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doLightningBolt, "Lightning Bolt", 8, false));
    public static final RegistryObject<Spell> KNOCKBACK_BOLT = register("knockback_bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doKnockBackBolt, "Knockback Bolt", 3, false));
    //public static final RegistryObject<Spell> PROTECTION_CIRCLE = register("protection_circle", () -> new Spell(SpellTypes.HELD, SpellActions::doProtectionCircle, "Protection Circle", 1, 6));
    public static final RegistryObject<Spell> RESPIRATION = register("respiration", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doRespiration, "Respiration", 1, 15, false));
    public static final RegistryObject<Spell> SPEED = register("speed", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doSpeed, "Speed", 1, 1, false));
    public static final RegistryObject<Spell> PIERCING_BOLT = register("piercing_bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doPiercingBolt, "Piercing Bolt", 12, false));
    public static final RegistryObject<Spell> ABSORBING = register("absorbing", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doAbsorbing, "Absorbing", 6, false));
    public static final RegistryObject<Spell> FANGS = register("fangs", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doFangsSpell, "Fangs", 6, true));
    public static final RegistryObject<Spell> FLAMING_BOLT = register("flaming_bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doFlamingBolt, "Flaming Bolt", 7, false));
    public static final RegistryObject<Spell> AQUA_BOLT = register("aqua_bolt", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doAquaBolt, "Aqua Bolt", 4, false));
    public static final RegistryObject<Spell> LURE = register("lure", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doLure, "Lure", 1, 10, false));
    //public static final RegistryObject<Spell> REPEL = register("repel", () -> new Spell(SpellTypes.HELD, SpellActions::doRepel, "Repel", 1, 3));
    public static final RegistryObject<Spell> FLAMING_CIRCLE = register("flaming_circle", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_ONE, SpellActions::doFlamingCircle, "Flaming Circle", 1, 6, false));
    public static final RegistryObject<Spell> FROST_PATH =  register("frost_path", () -> new Spell(SpellTypes.HELD, SpellTypes.SpellRarities.COMMON, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doFrostPath, "Frost Path", 1, 6, false));
    public static final RegistryObject<Spell> WITHER_SKULL = register("wither_skull", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.UNCOMMON, SpellTypes.MarkerTypes.COMBAT, SpellTypes.SpellTiers.TIER_THREE, SpellActions::doWitherSkull, "Wither Skull", 10, false));
    public static final RegistryObject<Spell> TORPEDO = register("torpedo", () -> new Spell(SpellTypes.CAST, SpellTypes.SpellRarities.RARE, SpellTypes.MarkerTypes.UTILITY, SpellTypes.SpellTiers.TIER_TWO, SpellActions::doTorpedo, "Torpedo", 8, false));
    public static Supplier<IForgeRegistry<Spell>> SPELLS_REGISTRY = SPELLS_DEFERRED_REGISTER.makeRegistry(Spell.class, () ->
            new RegistryBuilder<Spell>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, oldObj) -> {
                    })
                    .setDefaultKey(new ResourceLocation(HLSpells.MODID, "empty")));

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static RegistryObject<Spell> register(String name, Supplier<Spell> spell) {
        RegistryObject<Spell> registryObject = SPELLS_DEFERRED_REGISTER.register(name, spell);
        HLSpells.LOGGER.info("Spell added : " + spell.get().getTrueDisplayName() + " ");
        return registryObject;
    }
}
