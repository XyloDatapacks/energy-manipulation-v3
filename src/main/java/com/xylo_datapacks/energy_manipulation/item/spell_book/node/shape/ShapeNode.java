package com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;

public interface ShapeNode extends GenericNode {
    
    public abstract boolean generateShape(SpellExecutor spellExecutor);
    
}
