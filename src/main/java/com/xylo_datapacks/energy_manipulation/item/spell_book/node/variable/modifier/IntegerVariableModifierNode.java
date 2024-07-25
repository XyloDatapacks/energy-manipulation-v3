package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.NumberOperationNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.IntegerVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class IntegerVariableModifierNode extends AbstractNodeWithMap implements VariableModifierNode {
    SubNode<IntegerVariableGetterNode> variable = registerSubNode("variable", new SubNode.Builder<IntegerVariableGetterNode>()
            .addNodeValue(Nodes.VARIABLE_GETTER_INTEGER));
    SubNode<IntegerNumberNode> value = registerSubNode("value", SubNodes.INTEGER);
    
    public IntegerVariableModifierNode() {
        super(Nodes.VARIABLE_MODIFIER_INTEGER);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* VariableModifierNode Interface */

    @Override
    public boolean modifyVariable(SpellExecutor spellExecutor) {
        return variable.getNode().setVariable(spellExecutor, value.getNode().getIntegerNumber(spellExecutor));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
