package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface IntegerNumberNode extends NumberNode {

    public abstract Integer getIntegerNumber(SpellExecutor spellExecutor);
    
    public default Number getNumber(SpellExecutor spellExecutor) {
        return getIntegerNumber(spellExecutor);
    }
}
