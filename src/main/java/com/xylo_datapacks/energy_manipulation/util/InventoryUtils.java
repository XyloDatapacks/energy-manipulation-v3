package com.xylo_datapacks.energy_manipulation.util;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class InventoryUtils {

    public static NbtList toTag(SimpleInventory inventory) {
        NbtList tag = new NbtList();

        for(int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                NbtCompound stackTag = stack.writeNbt(new NbtCompound());
                stackTag.putByte("Slot", (byte) i); 
                tag.add(stackTag);
            }
        }
        return tag;
    }

    public static void fromTag(NbtList tag, SimpleInventory inventory) {
        inventory.clear();

        tag.forEach(element -> {
            NbtCompound stackTag = (NbtCompound) element;
            int slot = stackTag.getInt("Slot");
            stackTag.remove("Slot");
            ItemStack stack = ItemStack.fromNbt(stackTag);
            inventory.setStack(slot, stack);
        });
    }
}