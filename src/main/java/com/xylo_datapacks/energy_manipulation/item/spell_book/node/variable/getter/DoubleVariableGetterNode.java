package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class DoubleVariableGetterNode extends AbstractVariableGetterNode<Double> implements DoubleNumberNode {
    
    public DoubleVariableGetterNode() {
        super(Nodes.VARIABLE_GETTER_DOUBLE);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractVariableGetterNode Interface */

    @Override
    public Double getVariable(SpellExecutor spellExecutor) {
        String variableName = this.variableName.getNode().getString(spellExecutor);
        if (spellExecutor.getVariable(variableName) instanceof Double var) {
            return var;
        }
        return null;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* DoubleNumberNode Interface */

    @Override
    public Double getDoubleNumber(SpellExecutor spellExecutor) {
        return getVariable(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
