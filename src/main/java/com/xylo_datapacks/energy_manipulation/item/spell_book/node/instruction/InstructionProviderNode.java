package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithList;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;

import java.util.List;


public class InstructionProviderNode extends AbstractNodeWithList<InstructionNode> {
    
    public InstructionProviderNode() {
        super(Nodes.INSTRUCTION_PROVIDER, "instruction", new SubNode.Builder<InstructionNode>()
                .addNodeValues(List.of(
                        Nodes.INSTRUCTION_GENERATE_SHAPE,
                        Nodes.INSTRUCTION_MODIFY_POSITION)
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
    /* GenericNode Interface */

    @Override
    public void resumeExecution(SpellExecutor spellExecutor, List<String> path) {
        int nextInstructionIndex = !path.isEmpty() ? GenericNode.stripIndexFromPathElement(path.get(0)) + 1 : 0;
        super.resumeExecution(spellExecutor, path);
        runInstructionFromIndex(spellExecutor, nextInstructionIndex);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
