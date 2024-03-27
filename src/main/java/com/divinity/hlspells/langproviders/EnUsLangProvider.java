package com.divinity.hlspells.langproviders;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

public class EnUsLangProvider extends LanguageProvider {

    public EnUsLangProvider(DataGenerator gen) {
        super(gen, HLSpells.MODID, "en_us");
    }

    @Override
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    protected void addTranslations() {
        SpellInit.SPELLS.getEntries().forEach(s -> this.add(this.spellNameToRegistryName(s.get().getTrueDisplayName()), s.get().getTrueDisplayName()));
        EntityInit.ENTITIES.getEntries().forEach(e -> this.add(e.get(), WordUtils.capitalizeFully(ForgeRegistries.ENTITY_TYPES.getKey(e.get()).toString().replace("hlspells:", "").replace("_", " "))));
        BlockInit.BLOCKS.getEntries().forEach(b -> this.add(b.get(), WordUtils.capitalizeFully(ForgeRegistries.BLOCKS.getKey(b.get()).toString().replace("hlspells:", "").replace("_", " "))));
        EnchantmentInit.ENCHANTMENTS.getEntries().forEach(e -> this.add(e.get(), WordUtils.capitalizeFully(ForgeRegistries.ENCHANTMENTS.getKey(e.get()).toString().replace("hlspells:", "").replace("_", " "))));
        ItemInit.ITEMS.getEntries().stream().filter(i -> !(i.get() instanceof BlockItem)).forEach(i -> this.add(i.get(), WordUtils.capitalizeFully(ForgeRegistries.ITEMS.getKey(i.get()).toString().replace("hlspells:", "").replace("_", " "))));
        MenuTypeInit.MENUS.getEntries().forEach(m -> this.add(this.menuTypeRegistryName(m.get()), WordUtils.capitalizeFully(ForgeRegistries.MENU_TYPES.getKey(m.get()).toString().replace("hlspells:", "").replace("_", " "))));
        this.add("entity.minecraft.villager.hlspells.mage", "Mage");
        this.add("container.spell.clue","%s");
        this.add("container.spell.level.requirement" , "Level Requirement: %s");
        this.add("container.spell.button.requirement", "Transfer (Levels: %s)");
        this.add("container.spell.material.one", "1 %s");
        this.add("container.spell.material.many", "%s %s");
        this.add("sounds.subtitle.altar_transfer", "Spell Transfers");
        this.add("sounds.subtitle.cast_bolt", "Cast Bolt");
        this.add("sounds.subtitle.cast_flame", "Cast Flame");
        this.add("sounds.subtitle.cast_ice", "Cast Ice");
        this.add("sounds.subtitle.cast_necromancy", "Ominous Hum");
        this.add("sounds.subtitle.charge_combat", "Prepares Spell");
        this.add("sounds.subtitle.charge_utility", "Prepares Spell");
        this.add("sounds.subtitle.held_combat", "Ethereal Hum");
        this.add("sounds.subtitle.held_illuminate", "Ethereal Hum");
        this.add("sounds.subtitle.held_utility", "Ethereal Hum");
        this.add("sounds.subtitle.miscast_sound", "Cast Failure");
        this.add("sounds.subtitle.spell_attunement", "Spell Attuned");
    }

    private String spellNameToRegistryName(String spellName) {
        return "spell." + HLSpells.MODID + "." + spellName.toLowerCase().replace(" ", "_");
    }

    private String menuTypeRegistryName(MenuType<?> menuType) {
        return "container." + menuType.toString().replace(":", ".");
    }
}
