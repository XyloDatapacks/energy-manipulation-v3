package com.xylo_datapacks.energy_manipulation;

import com.xylo_datapacks.energy_manipulation.block.ModBlocks;
import com.xylo_datapacks.energy_manipulation.component.ModDataComponentTypes;
import com.xylo_datapacks.energy_manipulation.entity.ModEntityType;
import com.xylo_datapacks.energy_manipulation.item.ModItems;
import com.xylo_datapacks.energy_manipulation.registry.*;
import com.xylo_datapacks.energy_manipulation.network.ModPackets;
import com.xylo_datapacks.energy_manipulation.screen.ModScreenHandlerType;
import net.fabricmc.api.ModInitializer;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnergyManipulation implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("energy_manipulation");
	public static final String MOD_ID = "energy_manipulation";
	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
	public static MutableText itemTranslation(String name) {
		return Text.translatable("item." + MOD_ID + "." + name);
	}
	
	

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing " + MOD_ID);
		
		ModCreativeTabRegistry.init();
		ModItems.init();
		ModBlocks.init();
		ModEntityType.init();
		ModDataComponentTypes.init();
		ModScreenHandlerType.init();
		
		ModPackets.initServer();
	}
}