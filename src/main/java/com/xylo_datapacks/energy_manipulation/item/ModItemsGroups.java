package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemsGroups {
    
    public static final ItemGroup ENERGYMANIPULATION_GROUP = Registry.register(Registries.ITEM_GROUP, 
        new Identifier(EnergyManipulation.MOD_ID, "energy_manipulation"),
        FabricItemGroup
            .builder()
            .displayName(Text.translatable("itemgroup.energy_manipulation"))
            .icon(() -> new ItemStack(ModItems.SPELL_BOOK))
            .entries((displayContext, entries) -> {
                
                // Add items and blocks to group
                entries.add(ModItems.SPELL_BOOK);
                entries.add(ModItems.SPELL_BOOK_PAGE);
                
                
            })
            .build());
    
    public static void registerItemGroups() {
        EnergyManipulation.LOGGER.info("Registering Mod Item Groups for " + EnergyManipulation.MOD_ID);
    }
}
