package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface DoubleOperatorNode extends GenericNode {
    
    public abstract Double applyDoubleOperator(SpellExecutor spellExecutor, Double a, Double b);
    
}
