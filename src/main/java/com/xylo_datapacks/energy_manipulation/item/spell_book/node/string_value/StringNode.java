package com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface StringNode extends GenericNode {
    
    public abstract String getString(SpellExecutor spellExecutor);
}
