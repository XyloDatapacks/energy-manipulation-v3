package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;

import java.util.List;


public class InstructionProviderNode extends AbstractRunnableNodeWithList<InstructionNode, Integer> {
    
    public InstructionProviderNode() {
        super(Nodes.INSTRUCTION_PROVIDER, "instruction", new SubNode.Builder<InstructionNode>()
                .addNodeValues(List.of(
                        Nodes.INSTRUCTION_GENERATE_SHAPE,
                        Nodes.INSTRUCTION_MODIFY_POSITION,
                        Nodes.INSTRUCTION_IF,
                        Nodes.INSTRUCTION_DELAY,
                        Nodes.INSTRUCTION_BREAK,
                        Nodes.INSTRUCTION_CONTINUE,
                        Nodes.INSTRUCTION_WHILE_LOOP)
                ));
    }
    
    /** 
     * runs instructions 
     * @return instruction count
     */
    public int runInstructions(SpellExecutor spellExecutor) {
        return this.runNode(spellExecutor);
    }
    
    private int run(SpellExecutor spellExecutor) {
        return runFromIndex(spellExecutor, 0);
    }

    private int runFromIndex(SpellExecutor spellExecutor, int index) {
        int instructionCount = 0;
        for (int i = index; i < getSubNodesSize(); i++) {
            instructionCount++;
            execute(i, instruction -> instruction.executeInstruction(spellExecutor));
            /* instruction provider is a list of instructions, so should always stop
             * with any return type */
            if (spellExecutor.getExecutionData().returnType != ReturnType.NONE) {
                return instructionCount;
            }
        }
        resetExecution(spellExecutor);
        return instructionCount;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    @Override
    public Integer newExecution(SpellExecutor spellExecutor) {
        return run(spellExecutor);
    }

    @Override
    public Integer resumeExecution(SpellExecutor spellExecutor) {
        /* at this point we are at the line after the call:
         * "execute(i, instruction -> instruction.executeInstruction(spellExecutor));" */
        
        /* instruction provider is a list of instructions, so should always stop
         * with any return type */
        if (spellExecutor.getExecutionData().returnType != ReturnType.NONE) {
            return 0; // we cannot run any instruction
        }
        return runFromIndex(spellExecutor, getLastExecutedIndex() + 1);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
