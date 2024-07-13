package com.xylo_datapacks.energy_manipulation.util;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;

import javax.naming.Context;

public class EnergyManipulationModelPlugin implements ModelLoadingPlugin {
    
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.addModels(EnergyManipulation.id("item/spell_book_inv"));
    }
}