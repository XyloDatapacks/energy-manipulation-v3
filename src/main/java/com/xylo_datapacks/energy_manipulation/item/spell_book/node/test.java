package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.wispforest.endec.impl.KeyedEndec;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public class test {
    public static void main(String[] args) {

        NbtCompound nbt = new NbtCompound();
        nbt.putString("b", "value");
        
        NbtCompound nbt2 = new NbtCompound();
        nbt2.putString("c", "value2");
        
        try {
            NbtPathArgumentType.NbtPath path = NbtPathArgumentType.NbtPath.parse("key.a.c");
            path.put(nbt, nbt2);
            //path.put(nbt, nbt2);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        System.out.println(nbt);
        System.out.println(nbt2);
        
    }
}
