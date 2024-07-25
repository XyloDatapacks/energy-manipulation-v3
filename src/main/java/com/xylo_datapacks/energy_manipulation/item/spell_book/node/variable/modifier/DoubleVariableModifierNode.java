package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.DoubleVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class DoubleVariableModifierNode extends AbstractNodeWithMap implements VariableModifierNode {
    SubNode<DoubleVariableGetterNode> variable = registerSubNode("variable", new SubNode.Builder<DoubleVariableGetterNode>()
            .addNodeValue(Nodes.VARIABLE_GETTER_DOUBLE));
    SubNode<DoubleNumberNode> value = registerSubNode("value", SubNodes.DOUBLE);
    
    public DoubleVariableModifierNode() {
        super(Nodes.VARIABLE_MODIFIER_DOUBLE);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* VariableModifierNode Interface */

    @Override
    public boolean modifyVariable(SpellExecutor spellExecutor) {
        return variable.getNode().setVariable(spellExecutor, value.getNode().getDoubleNumber(spellExecutor));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
