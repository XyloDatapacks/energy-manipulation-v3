package com.xylo_datapacks.energy_manipulation.datagen;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> CATALYSTS = TagKey.of(RegistryKeys.ITEM, EnergyManipulation.id("catalysts"));
    public static final TagKey<Item> ADVANCED_CATALYSTS = TagKey.of(RegistryKeys.ITEM, EnergyManipulation.id("advanced_catalysts"));


    public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(CATALYSTS)
                .add(ModItems.BASIC_CATALYST)
                .add(ModItems.SPIKE_CATALYST)
                .add(ModItems.PLATE_CATALYST);

        getOrCreateTagBuilder(ADVANCED_CATALYSTS);
    }
}
