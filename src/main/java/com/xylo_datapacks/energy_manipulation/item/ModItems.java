package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.component.ModDataComponentTypes;
import com.xylo_datapacks.energy_manipulation.component.type.CatalystComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {

    public static final Item SPELL_BOOK = registerItem("spell_book", new SpellBookItem(new Item.Settings().maxDamage(100)));
    public static final Item SPELL_BOOK_PAGE = registerItem("spell_book_page", new SpellBookPageItem(new Item.Settings()));
    public static final Item BASIC_CATALYST = registerItem("basic_catalyst", new CatalystItem(new Item.Settings().component(ModDataComponentTypes.CATALYST, CatalystComponents.BASIC)));
    public static final Item SPIKE_CATALYST = registerItem("spike_catalyst", new CatalystItem(new Item.Settings().component(ModDataComponentTypes.CATALYST, CatalystComponents.SPIKE)));
    public static final Item PLATE_CATALYST = registerItem("plate_catalyst", new CatalystItem(new Item.Settings().component(ModDataComponentTypes.CATALYST, CatalystComponents.PLATE)));
    public static final Item DUMMY_DISPLAY = registerItem("dummy_display", new Item(new Item.Settings()));
    
    // Function called to add items
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, EnergyManipulation.id(name), item);
    }

    // Register items in different item groups
    public static void init()
    {
        EnergyManipulation.LOGGER.info("Registering Mod Items for " + EnergyManipulation.MOD_ID);
    }
}
