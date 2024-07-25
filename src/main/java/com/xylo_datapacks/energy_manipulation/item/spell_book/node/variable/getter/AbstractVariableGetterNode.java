package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public abstract class AbstractVariableGetterNode<T> extends AbstractNodeWithMap {
    SubNode<StringNode> variableName = registerSubNode("variable_name", SubNodes.STRING);
    
    public AbstractVariableGetterNode(NodeData<?> nodeData) {
        super(nodeData);
    }

    public abstract T getVariable(SpellExecutor spellExecutor);

    public boolean setVariable(SpellExecutor spellExecutor, T value) {
        String variableName = this.variableName.getNode().getString(spellExecutor);
        return spellExecutor.setVariable(variableName, value);
    }
}
