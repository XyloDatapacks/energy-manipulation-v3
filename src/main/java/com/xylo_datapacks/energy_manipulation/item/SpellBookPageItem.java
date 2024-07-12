package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.InstructionProviderNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.ModifyPositionInstructionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class SpellBookPageItem extends Item implements FabricItem {
    private static final String SPELL_KEY = "spell";
    
    public SpellBookPageItem(Item.Settings settings) {
        super(settings);
    }

    
    public static void setSpell(ItemStack stack, NbtCompound spell) {
        System.out.println("NEW INPUT: " + spell);
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        System.out.println("INITIAL NBT: " + nbtCompound);
        nbtCompound.put(SPELL_KEY, spell);
        System.out.println("FINAL NBT: " + nbtCompound);
    }

    public static GenericNode getSpell(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return new SpellNode();
        }
        return GenericNode.generateFromNbt(nbtCompound.getCompound(SPELL_KEY));
    }
}
