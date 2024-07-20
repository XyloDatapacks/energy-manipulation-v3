package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;


public class InstructionProviderNode extends AbstractRunnableNodeWithList<InstructionNode, Integer> {
    
    public InstructionProviderNode() {
        super(Nodes.INSTRUCTION_PROVIDER, "instruction", SubNodes.COMMON_INSTRUCTION);
    }

    public InstructionProviderNode(SubNodes.SubNodeDefinition<InstructionNode> definition) {
        super(Nodes.INSTRUCTION_PROVIDER, "instruction", definition);
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
            if (shouldBlockExecution(spellExecutor)) {
                break;
            }
        }
        if (shouldReset(spellExecutor)) {
            resetExecution(spellExecutor);
        }
        return instructionCount;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    /*
     * should block execution for RETURN, BREAK, CONTINUE
     * should always reset unless it is RETURN
     */
    
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
        if (!shouldBlockExecution(spellExecutor)) {
            return runFromIndex(spellExecutor, getLastExecutedIndex() + 1); // we can continue if not interrupted again
        }
        
        if (shouldReset(spellExecutor)) {
            resetExecution(spellExecutor);
        }
        return 0; // we cannot run any instruction
        
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
