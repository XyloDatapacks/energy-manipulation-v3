package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface VariableModifierNode extends GenericNode {
    
    public abstract boolean modifyVariable(SpellExecutor spellExecutor);
    
}
