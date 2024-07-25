package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import io.wispforest.endec.impl.KeyedEndec;
import io.wispforest.owo.ui.core.Color;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public class test {
    public static void main(String[] args) {
        
        Color START_COLOR = new Color(4, 56, 47);
        Color END_COLOR = new Color(189, 242, 233);
        Color HOVERED_COLOR = new Color(14, 46, 40);
        Color DISABLED_COLOR = new Color(36, 54, 50);
        System.out.println(START_COLOR.red()/255 + ", " + START_COLOR.green()/255 + ", " + START_COLOR.blue()/255);
        System.out.println(END_COLOR.red()/255 + ", " + END_COLOR.green()/255 + ", " + END_COLOR.blue()/255);
        System.out.println(HOVERED_COLOR.red()/255 + ", " + HOVERED_COLOR.green()/255 + ", " + HOVERED_COLOR.blue()/255);
        System.out.println(DISABLED_COLOR.red()/255 + ", " + DISABLED_COLOR.green()/255 + ", " + DISABLED_COLOR.blue()/255);
        
    }
}
