package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface DoubleNumberNode extends NumberNode {

    public default Double getDoubleNumber(SpellExecutor spellExecutor) {
        return (Double) getNumber(spellExecutor);
    }
    
}
