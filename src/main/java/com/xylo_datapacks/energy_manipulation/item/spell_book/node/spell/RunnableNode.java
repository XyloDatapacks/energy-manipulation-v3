package com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;

public interface RunnableNode extends GenericNode {
    
    public abstract void executeSpell(SpellExecutor spellExecutor);
    
}
