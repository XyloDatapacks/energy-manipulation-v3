package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockRegistry {



    // Function called to add the block
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, EnergyManipulation.id(name), block);
    }

    // Function called to add the item associated to the block
    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, EnergyManipulation.id(name),
                new BlockItem(block, new Item.Settings()));
    }

    // Register blocks in different item groups
    public static void init() {
        EnergyManipulation.LOGGER.info("Registering Mod Blocks for" + EnergyManipulation.MOD_ID);
    }
}
