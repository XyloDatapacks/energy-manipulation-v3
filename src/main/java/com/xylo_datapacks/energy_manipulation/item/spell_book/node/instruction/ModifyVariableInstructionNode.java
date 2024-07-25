package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.NumberOperationNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier.VariableModifierNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class ModifyVariableInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<VariableModifierNode> modifier = registerSubNode("modifier", new SubNode.Builder<VariableModifierNode>()
            .addNodeValues(List.of(
                    Nodes.VARIABLE_MODIFIER_INTEGER,
                    Nodes.VARIABLE_MODIFIER_DOUBLE,
                    Nodes.VARIABLE_MODIFIER_STRING))
    );
    
    public ModifyVariableInstructionNode() {
        super(Nodes.INSTRUCTION_MODIFY_VARIABLE);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return modifier.getNode().modifyVariable(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
