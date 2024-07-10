package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithList;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;

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
}
