package com.xylo_datapacks.energy_manipulation.screen;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModScreenHandlerType {

    // spell book menu
    public static final ExtendedScreenHandlerType<SpellBookScreenHandler, SpellBookItem.SpellBookMenuData> SPELL_BOOK_MENU_TYPE = Registry.register(Registries.SCREEN_HANDLER, EnergyManipulation.id("spell_book_menu"), new ExtendedScreenHandlerType<>(SpellBookScreenHandler::new, SpellBookItem.SpellBookMenuData.PACKET_CODEC));



    public static void init()
    {
        EnergyManipulation.LOGGER.info("Registering Mod Menus for " + EnergyManipulation.MOD_ID);
    }
}
