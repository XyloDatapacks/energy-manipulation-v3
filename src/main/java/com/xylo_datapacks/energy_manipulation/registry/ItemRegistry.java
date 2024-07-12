package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.item.SpellBookPageItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItemRegistry {

    public static final Item SPELL_BOOK = registerItem("spell_book", new SpellBookItem(new Item.Settings().component(DataComponentTypes.MAX_DAMAGE, 100)));
    public static final Item SPELL_BOOK_PAGE = registerItem("spell_book_page", new SpellBookPageItem(new Item.Settings()));
    
    
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
