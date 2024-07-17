package com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.InstructionProviderNode;

import java.util.List;

public class SpellNode extends AbstractRunnableNodeWithMap {
    SubNode<InstructionProviderNode> program = registerSubNode("program", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValues(List.of(
                    Nodes.INSTRUCTION_PROVIDER))
            .build(this));
    
    public SpellNode() {
        super(Nodes.SPELL);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* SpellNode Interface */
    
    public void executeSpell(SpellExecutor spellExecutor) {
        System.out.println("Executing spell node");
        program.getNode().runInstructions(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    @Override
    public void execute(SpellExecutor spellExecutor) {

    }

    @Override
    public void resumeExecution(SpellExecutor spellExecutor) {
        super.resumeExecution(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
