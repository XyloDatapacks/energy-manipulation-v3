package com.xylo_datapacks.energy_manipulation.util;

import com.xylo_datapacks.energy_manipulation.item.ModItems;
import com.xylo_datapacks.energy_manipulation.item.custom.SpellBookItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ModModelPredicateProvider {
    
    public static void registerModModels() {
        registerSpellBook(ModItems.SPELL_BOOK);
    }
    
    private static void registerSpellBook(Item spellBook) {
        
        ModelPredicateProviderRegistry.register(spellBook, new Identifier("charge"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0f;
            }
            if (SpellBookItem.isCharged(stack)) {
                return SpellBookItem.getCharge(stack);
            }
            return (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (float)SpellBookItem.getPullTime(stack);
        });
        ModelPredicateProviderRegistry.register(spellBook, new Identifier("charging"), (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !SpellBookItem.isCharged(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(spellBook, new Identifier("charged"), (stack, world, entity, seed) -> SpellBookItem.isCharged(stack) ? 1.0f : 0.0f);

    }
    
}
