package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface OperationNode extends GenericNode {
    
    public void performOperation(SpellExecutor spellExecutor);
    
}
