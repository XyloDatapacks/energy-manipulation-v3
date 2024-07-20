package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import io.wispforest.endec.impl.KeyedEndec;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public class test {
    public static void main(String[] args) {

        ReturnType returnTypeOg = ReturnType.BREAK;

        ReturnType returnType = returnTypeOg;
        returnTypeOg = ReturnType.NONE;
        System.out.println(returnType);
        System.out.println(returnTypeOg);
        
    }
}
