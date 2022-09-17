package com.divinity.hlspells.langproviders;

import com.divinity.hlspells.HLSpells;
import com.divinity.hlspells.setup.init.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.common.data.LanguageProvider;
import org.apache.commons.lang3.text.WordUtils;

public class EnUsLangProvider extends LanguageProvider {

    public EnUsLangProvider(DataGenerator gen) {
        super(gen, HLSpells.MODID, "en_us");
    }

    @Override
    @SuppressWarnings({"deprecation", "ConstantConditions"})
    protected void addTranslations() {
        SpellInit.SPELLS.getEntries().forEach(s -> this.add(this.spellNameToRegistryName(s.get().getTrueDisplayName()), s.get().getTrueDisplayName()));
        EntityInit.ENTITIES.getEntries().forEach(e -> this.add(e.get(), WordUtils.capitalizeFully(e.get().getRegistryName().toString().replace("hlspells:", "").replace("_", " "))));
        BlockInit.BLOCKS.getEntries().forEach(b -> this.add(b.get(), WordUtils.capitalizeFully(b.get().getRegistryName().toString().replace("hlspells:", "").replace("_", " "))));
        EnchantmentInit.ENCHANTMENTS.getEntries().forEach(e -> this.add(e.get(), WordUtils.capitalizeFully(e.get().getRegistryName().toString().replace("hlspells:", "").replace("_", " "))));
        ItemInit.ITEMS.getEntries().stream().filter(i -> !(i.get() instanceof BlockItem)).forEach(i -> this.add(i.get(), WordUtils.capitalizeFully(i.get().getRegistryName().toString().replace("hlspells:", "").replace("_", " "))));
        MenuTypeInit.MENUS.getEntries().forEach(m -> this.add(this.menuTypeRegistryName(m.get()), WordUtils.capitalizeFully(m.get().getRegistryName().toString().replace("hlspells:", "").replace("_", " "))));
        this.add("entity.minecraft.villager.hlspells.mage", "Mage");
        this.add("container.spell.clue","%s");
        this.add("container.spell.level.requirement" , "Level Requirement: %s");
        this.add("container.spell.material.one", "1 %s");
        this.add("container.spell.material.many", "%s %s");
    }

    private String spellNameToRegistryName(String spellName) {
        return "spell." + HLSpells.MODID + "." + spellName.toLowerCase().replace(" ", "_");
    }

    private String menuTypeRegistryName(MenuType<?> menuType) {
        return "container." + menuType.getRegistryName().toString().replace(":", ".");
    }
}
