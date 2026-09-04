package com.divinity.hlspells.setup;

import com.divinity.hlspells.capabilities.playercap.PlayerCapProvider;
import com.divinity.hlspells.capabilities.spellholdercap.SpellHolderProvider;
import com.divinity.hlspells.capabilities.totemcap.TotemItemProvider;
import com.divinity.hlspells.items.armor.material.WizardArmorMaterial;
import com.divinity.hlspells.setup.init.BlockInit;
import com.divinity.hlspells.setup.init.EntityInit;
import com.divinity.hlspells.setup.init.ItemInit;
import com.divinity.hlspells.setup.init.LootInit;
import com.divinity.hlspells.setup.init.MenuTypeInit;
import com.divinity.hlspells.setup.init.ParticlesInit;
import com.divinity.hlspells.setup.init.SoundInit;
import com.divinity.hlspells.setup.init.SpellInit;
import com.divinity.hlspells.setup.init.VillagerInit;
import net.neoforged.bus.api.IEventBus;

public class ModRegistry {

    public static void init(IEventBus bus) {
        ParticlesInit.PARTICLE_TYPES.register(bus);
        EntityInit.ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);
        WizardArmorMaterial.ARMOR_MATERIALS.register(bus);
        LootInit.LOOT_MODIFIER_SERIALIZERS.register(bus);
        LootInit.LOOT_FUNCTIONS.register(bus);
        SpellInit.SPELLS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockInit.BLOCK_ENTITIES.register(bus);
        MenuTypeInit.MENUS.register(bus);
        VillagerInit.POI.register(bus);
        SoundInit.SOUNDS.register(bus);
        VillagerInit.PROFESSIONS.register(bus);
        PlayerCapProvider.ATTACHMENTS.register(bus);
        SpellHolderProvider.COMPONENTS.register(bus);
        TotemItemProvider.COMPONENTS.register(bus);
    }
}
