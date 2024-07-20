package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class WhileLoopInstructionNode extends AbstractRunnableNodeWithMap<Boolean> implements InstructionNode {
    SubNode<BooleanNode> condition = registerSubNode(SubNodes.CONDITION);
    SubNode<InstructionProviderNode> body = registerSubNode("body", SubNodes.LOOP_INSTRUCTION_PROVIDER);

    
    public WhileLoopInstructionNode() {
        super(Nodes.INSTRUCTION_WHILE_LOOP);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    /**
     * while loop
     * @return always true
     */
    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return this.runNode(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    /* 
     * RETURN: should always block execution and not cause a reset 
     * BREAK: should block the loop but reset the returnType and cause a reset
     * CONTINUE: should continue the loop but reset the returnType
     * end execution with NONE: cause a reset
     * 
     * -> return on RETURN. on BREAK and CONTINUE reset returnType. BREAK stops loop
     *    if ends with NONE (or BREAK) reset execution
     */
    
    
    @Override
    public Boolean newExecution(SpellExecutor spellExecutor) {
        System.out.println("NEW WHILE LOOP");
        
        loop: while (condition.getNode().getBoolean(spellExecutor)) {
            execute(body).runInstructions(spellExecutor);
            
            if (shouldBlockExecution(spellExecutor)) {
                ReturnType returnType = spellExecutor.getExecutionData().returnType;
                if (returnType == ReturnType.RETURN) return true; 
                // break or continue:
                spellExecutor.getExecutionData().returnType = ReturnType.NONE;
                if (returnType == ReturnType.BREAK) break loop;
            }
        }
        if (shouldReset(spellExecutor)) {
            System.out.println("reset while loop");
            resetExecution(spellExecutor);
        }
        return true;
    }

    @Override
    public Boolean resumeExecution(SpellExecutor spellExecutor) {
        System.out.println("RESUMED WHILE LOOP");
        /* at this point we are at the line after the call:
         * "execute(body).runInstructions(spellExecutor);" */

        if (shouldBlockExecution(spellExecutor)) {
            ReturnType returnType = spellExecutor.getExecutionData().returnType;
            if (returnType == ReturnType.RETURN) return true;
            // break or continue:
            spellExecutor.getExecutionData().returnType = ReturnType.NONE;
            if (returnType == ReturnType.BREAK) {
                resetExecution(spellExecutor);
                return true;
            }
        }
        return newExecution(spellExecutor); // restart the loop if return NONE or CONTINUE
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
