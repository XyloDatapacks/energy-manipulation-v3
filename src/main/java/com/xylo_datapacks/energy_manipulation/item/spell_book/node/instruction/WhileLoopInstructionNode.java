package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class WhileLoopInstructionNode extends AbstractRunnableNodeWithMap<Boolean> implements InstructionNode {
    SubNode<BooleanNode> condition = registerSubNode(SubNodes.CONDITION);
    SubNode<InstructionProviderNode> body = registerSubNode("body", SubNodes.INSTRUCTIONS.subNodeBuilderTemplate());

    
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

    @Override
    public Boolean newExecution(SpellExecutor spellExecutor) {
        loop: while (condition.getNode().getBoolean(spellExecutor)) {
            execute(body).runInstructions(spellExecutor);
            /* while loop runs a list of instructions, and should respond as a while loop to the return type */
            switch (spellExecutor.getExecutionData().returnType) {
                // the instructions called a "return", so we should exit the cycle
                case RETURN: return true;
                // the instructions called a "continue", so we should just go on, and consume the "continue"
                case CONTINUE: {
                    spellExecutor.getExecutionData().returnType = ReturnType.NONE;
                    continue loop;
                }
                // the instructions called a "break", so we should break out of the loop, and consume the "break"
                case BREAK: {
                    spellExecutor.getExecutionData().returnType = ReturnType.NONE;
                    break loop;
                }
            }
        }
        if (spellExecutor.getExecutionData().returnType == ReturnType.NONE) resetExecution(spellExecutor);
        return true;
    }

    @Override
    public Boolean resumeExecution(SpellExecutor spellExecutor) {
        /* at this point we are at the line after the call:
         * "execute(body).runInstructions(spellExecutor);" */
        
        /* while loop runs a list of instructions, and should respond as a while loop to the return type */
        switch (spellExecutor.getExecutionData().returnType) {
            // the instructions called a "return", so we should exit the cycle
            case RETURN: return true;
            // the instructions called a "continue", so we should just go on, and consume the "continue"
            case CONTINUE: {
                spellExecutor.getExecutionData().returnType = ReturnType.NONE;
                break; // break out of the switch, so we can run the loop again
            }
            // the instructions called a "break", so we should break out of the loop, and consume the "break"
            case BREAK: {
                spellExecutor.getExecutionData().returnType = ReturnType.NONE;
                return true; // we don't want to start another loop
            }
        }
        return newExecution(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
