package com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;

public interface BooleanNode extends GenericNode {

    public abstract Boolean getBoolean(SpellExecutor spellExecutor);
}
