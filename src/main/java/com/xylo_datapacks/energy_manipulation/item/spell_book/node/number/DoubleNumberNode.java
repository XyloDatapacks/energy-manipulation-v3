package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface DoubleNumberNode extends NumberNode {

    public abstract Double getDoubleNumber(SpellExecutor spellExecutor);

    public default Number getNumber(SpellExecutor spellExecutor) {
        return getDoubleNumber(spellExecutor);
    }
}
