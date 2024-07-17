package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;

public interface InstructionNode extends GenericNode {

    public abstract boolean executeInstruction(SpellExecutor spellExecutor);
}
