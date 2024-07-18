package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

public enum ReturnType {
    /** Should not interrupt execution */
    NONE,
    /** Should stop any execution, and not be changed */
    RETURN,
    /** Should break first loop, and be changed to NONE */
    BREAK,
    /** Should continue first loop, and be changed to NONE */
    CONTINUE
}
