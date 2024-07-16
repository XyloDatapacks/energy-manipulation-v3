package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.item.ModItems;
import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    
    public static void registerModModels() {
        registerSpellBook(ModItems.SPELL_BOOK);
    }
    
    private static void registerSpellBook(Item spellBook) {
        ModelPredicateProviderRegistry.register(
                spellBook, Identifier.ofVanilla("charge"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    } 
                    else if (SpellBookItem.isCharged(stack)) {
                        return SpellBookItem.getCharge(stack);
                    }
                    return (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / (float)SpellBookItem.getPullTime(stack, entity);
                }
        );
        ModelPredicateProviderRegistry.register(
                spellBook, Identifier.ofVanilla("charging"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !SpellBookItem.isCharged(stack) ? 1.0F : 0.0F
        );
        ModelPredicateProviderRegistry.register(spellBook, Identifier.ofVanilla("charged"), (stack, world, entity, seed) -> SpellBookItem.isCharged(stack) ? 1.0F : 0.0F);
        ModelPredicateProviderRegistry.register(spellBook, Identifier.ofVanilla("advanced_catalyst"), (stack, world, entity, seed) -> {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
            return chargedProjectilesComponent != null && chargedProjectilesComponent.contains(Items.END_CRYSTAL) ? 1.0F : 0.0F;
        });
    }
    
}
