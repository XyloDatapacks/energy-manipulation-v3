package com.xylo_datapacks.energy_manipulation;

import com.xylo_datapacks.energy_manipulation.registry.*;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
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
		ModItemRegistry.init();
		ModBlockRegistry.init();
		ModEntityRegistry.init();
		ModDataComponentRegistry.init();
		ModScreenTypeRegistry.init();
		
		ModPackets.initServer();
	}
}