package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;
import java.util.Optional;

public class IfInstructionNode extends AbstractRunnableNodeWithMap<Boolean> implements InstructionNode {
    SubNode<BooleanNode> condition = registerSubNode(SubNodes.CONDITION);
    SubNode<InstructionProviderNode> passed = registerSubNode("passed", SubNodes.INSTRUCTIONS.subNodeBuilderTemplate());
    SubNode<InstructionProviderNode> failed = registerSubNode("failed", SubNodes.INSTRUCTIONS.subNodeBuilderTemplate());
    
    
    public IfInstructionNode() {
        super(Nodes.INSTRUCTION_IF);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    /**
     * execute instructions based on the condition
     * @return if condition passed
     */
    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return this.runNode(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */
    
    @Override
    public Boolean newExecution(SpellExecutor spellExecutor) {
        boolean isConditionTrue = condition.getNode().getBoolean(spellExecutor);
        
        if (isConditionTrue) {
            execute(passed).runInstructions(spellExecutor);
        }
        else {
            execute(failed).runInstructions(spellExecutor);
        }

        if (spellExecutor.getExecutionData().returnType == ReturnType.NONE) resetExecution(spellExecutor);
        return isConditionTrue;
    }

    @Override
    public Boolean resumeExecution(SpellExecutor spellExecutor) {
        /* at this point we are at the line after the call:
         * "execute(passed).runInstructions(spellExecutor);" or "execute(failed).runInstructions(spellExecutor);" */

        // get condition state before resetting 
        boolean wasConditionTrue = getLastExecuted().equals(passed.getId()); 
        
        if (spellExecutor.getExecutionData().returnType == ReturnType.NONE) resetExecution(spellExecutor);
        return wasConditionTrue;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
