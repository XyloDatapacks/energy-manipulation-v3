package com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.InstructionProviderNode;

public class SpellNode extends AbstractNodeWithMap {
    SubNode<InstructionProviderNode> program = registerSubNode("program", SubNodes.COMMON_INSTRUCTION_PROVIDER);
    
    public SpellNode() {
        super(Nodes.SPELL);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* SpellNode Interface */
    
    public void executeSpell(SpellExecutor spellExecutor) {
        program.getNode().runInstructions(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
