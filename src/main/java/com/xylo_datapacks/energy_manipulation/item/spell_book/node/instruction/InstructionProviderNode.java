package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.google.common.base.Supplier;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;

import java.security.Key;
import java.util.List;


public class InstructionProviderNode extends AbstractRunnableNodeWithList<InstructionNode> {
    
    public InstructionProviderNode() {
        super(Nodes.INSTRUCTION_PROVIDER, "instruction", new SubNode.Builder<InstructionNode>()
                .addNodeValues(List.of(
                        Nodes.INSTRUCTION_GENERATE_SHAPE,
                        Nodes.INSTRUCTION_MODIFY_POSITION,
                        Nodes.INSTRUCTION_DELAY,
                        Nodes.INSTRUCTION_IF)
                ));
    }
    
    public void runInstructions(SpellExecutor spellExecutor) {
        getSubNodes().forEach(instruction -> {
            instruction.getNode().executeInstruction(spellExecutor);
        });
    }

    public void runInstructionFromIndex(SpellExecutor spellExecutor, int index) {
        List<SubNode<InstructionNode>> subNodes = getSubNodes();
        for (int i = index; i < subNodes.size(); i++) {
            subNodes.get(i).getNode().executeInstruction(spellExecutor);
        }
    }

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
