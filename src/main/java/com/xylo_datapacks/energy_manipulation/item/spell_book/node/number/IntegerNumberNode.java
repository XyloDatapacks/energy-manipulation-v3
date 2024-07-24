package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface IntegerNumberNode extends NumberNode {

    public default Integer getIntegerNumber(SpellExecutor spellExecutor) {
        return (Integer) getNumber(spellExecutor);
    }
    
}
