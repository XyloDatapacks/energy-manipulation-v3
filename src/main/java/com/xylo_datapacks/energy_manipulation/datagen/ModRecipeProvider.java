package com.xylo_datapacks.energy_manipulation.datagen;

import com.xylo_datapacks.energy_manipulation.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SPELL_BOOK)
                .input(Items.ENCHANTED_BOOK)
                .input(Items.BLAZE_POWDER)
                .criterion(hasItem(Items.ENCHANTED_BOOK), conditionsFromItem(Items.ENCHANTED_BOOK))
                .criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.SPELL_BOOK)));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.SPELL_BOOK_PAGE, 1)
                .input(Items.PAPER)
                .input(Items.INK_SAC)
                .input(Items.BLAZE_POWDER)
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .criterion(hasItem(Items.INK_SAC), conditionsFromItem(Items.INK_SAC))
                .criterion(hasItem(Items.BLAZE_POWDER), conditionsFromItem(Items.BLAZE_POWDER))
                .offerTo(exporter, Identifier.of(getRecipeName(ModItems.SPELL_BOOK_PAGE)));
    }
}
