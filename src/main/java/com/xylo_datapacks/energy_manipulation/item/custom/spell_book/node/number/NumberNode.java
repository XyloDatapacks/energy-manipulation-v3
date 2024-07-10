package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;

public interface NumberNode extends GenericNode {
    
    public abstract Number getNumber(SpellExecutor spellExecutor);
}
