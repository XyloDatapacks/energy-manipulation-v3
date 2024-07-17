package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class IfInstructionNode extends AbstractRunnableNodeWithMap implements InstructionNode {
    SubNode<BooleanNode> condition = registerSubNode("condition", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN))
            .build(this));
    
    SubNode<InstructionProviderNode> passed = registerSubNode("passed", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValues(List.of(
                    Nodes.INSTRUCTION_PROVIDER))
            .build(this));
    
    SubNode<InstructionProviderNode> failed = registerSubNode("failed", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValues(List.of(
                    Nodes.INSTRUCTION_PROVIDER))
            .build(this));
    
    public IfInstructionNode() {
        super(Nodes.INSTRUCTION_IF);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        if (condition.getNode().getBoolean(spellExecutor)) {
            passed.getNode().runInstructions(spellExecutor);
        }
        else {
            failed.getNode().runInstructions(spellExecutor);
        }
        return true;
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
