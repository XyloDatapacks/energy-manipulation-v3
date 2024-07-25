package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.StringVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class StringVariableModifierNode extends AbstractNodeWithMap implements VariableModifierNode {
    SubNode<StringVariableGetterNode> variable = registerSubNode("variable", new SubNode.Builder<StringVariableGetterNode>()
            .addNodeValue(Nodes.VARIABLE_GETTER_STRING));
    SubNode<StringNode> value = registerSubNode("value", SubNodes.STRING);
    
    public StringVariableModifierNode() {
        super(Nodes.VARIABLE_MODIFIER_STRING);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* VariableModifierNode Interface */

    @Override
    public boolean modifyVariable(SpellExecutor spellExecutor) {
        return variable.getNode().setVariable(spellExecutor, value.getNode().getString(spellExecutor));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
