package com.xylo_datapacks.energy_manipulation.util;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    
    public static class Blocks {
        
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, EnergyManipulation.id(name));
        }
    }
    
    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, EnergyManipulation.id(name));
        }
    }
}
