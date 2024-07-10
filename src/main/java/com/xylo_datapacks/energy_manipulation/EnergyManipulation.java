package com.xylo_datapacks.energy_manipulation;

import com.xylo_datapacks.energy_manipulation.block.ModBlocks;
import com.xylo_datapacks.energy_manipulation.entity.ModEntities;
import com.xylo_datapacks.energy_manipulation.item.ModItems;
import com.xylo_datapacks.energy_manipulation.item.ModItemsGroups;
import com.xylo_datapacks.energy_manipulation.item.custom.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnergyManipulation implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("energy_manipulation");
	public static final String MOD_ID = "energy_manipulation";
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	// spell book
	public static final Identifier SPELL_BOOK_ID = SpellBookItem.id("spell_book");
	// spell book menu
	public static final ScreenHandlerType<SpellBookScreenHandler> SPELL_BOOK_MENU_TYPE = Registry.register(Registries.SCREEN_HANDLER, SPELL_BOOK_ID,
			new ExtendedScreenHandlerType<>(SpellBookScreenHandler::new));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");

		// Register custom groups
		ModItemsGroups.registerItemGroups();

		// Register mod additions
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEntities.registerModEntities();

		// Register Client RPCs (so server can receive client rpcs)
		ModPackets.registerC2SPackets();
	}
}