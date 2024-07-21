package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.OperationNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.VariableTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class OperationInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<OperationNode> operation = registerSubNode("operation", new SubNode.Builder<OperationNode>()
            .addNodeValues(List.of(
                    Nodes.OPERATION_NUMBER))
    );
    
    public OperationInstructionNode() {
        super(Nodes.INSTRUCTION_OPERATION);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return true;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
