package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface IntegerOperatorNode extends GenericNode {
    
    public abstract Integer applyIntegerOperator(SpellExecutor spellExecutor, Integer a, Integer b);
    
}
