package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public interface RunnableNode extends GenericNode {

    public abstract void execute(SpellExecutor spellExecutor);
    
    public abstract void resumeExecution(SpellExecutor spellExecutor);
}
