package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.item.custom.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.item.custom.SpellBookPageItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item SPELL_BOOK = registerItem("spell_book", new SpellBookItem(new Item.Settings().component(DataComponentTypes.MAX_DAMAGE, 100)));
    public static final Item SPELL_BOOK_PAGE = registerItem("spell_book_page", new SpellBookPageItem(new Item.Settings()));
    
    
    // Function called to add items
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, EnergyManipulation.id(name), item);
    }

    // Ingredient Group
    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
    }

    // Register items in different item groups
    public static void registerModItems()
    {
        EnergyManipulation.LOGGER.info("Registering Mod Items for " + EnergyManipulation.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
