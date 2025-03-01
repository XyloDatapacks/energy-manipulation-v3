package com.xylo_datapacks.energy_manipulation.datagen;

import com.xylo_datapacks.energy_manipulation.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        
       
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SPELL_BOOK_PAGE, Models.GENERATED);
        itemModelGenerator.register(ModItems.BASIC_CATALYST, Models.GENERATED);
        itemModelGenerator.register(ModItems.SPIKE_CATALYST, Models.GENERATED);
        itemModelGenerator.register(ModItems.PLATE_CATALYST, Models.GENERATED);
    }
}
