package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.VariableTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class CreateVariableInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<StringNode> name = registerSubNode("name", SubNodes.STRING);
    SubNode<VariableTypeNode> type = registerSubNode("type", new SubNode.Builder<VariableTypeNode>()
            .addNodeValue(Nodes.VARIABLE_TYPE));
    
    
    public CreateVariableInstructionNode() {
        super(Nodes.INSTRUCTION_CREATE_VARIABLE);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return true;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
