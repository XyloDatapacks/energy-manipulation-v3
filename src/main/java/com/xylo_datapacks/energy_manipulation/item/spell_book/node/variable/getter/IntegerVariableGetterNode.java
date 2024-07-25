package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class IntegerVariableGetterNode extends AbstractVariableGetterNode<Integer> implements IntegerNumberNode {
    
    public IntegerVariableGetterNode() {
        super(Nodes.VARIABLE_GETTER_INTEGER);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractVariableGetterNode Interface */

    @Override
    public Integer getVariable(SpellExecutor spellExecutor) {
        String variableName = this.variableName.getNode().getString(spellExecutor);
        if (spellExecutor.getVariable(variableName) instanceof Integer var) {
            return var;
        }
        return null;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* IntegerNumberNode Interface */

    @Override
    public Integer getIntegerNumber(SpellExecutor spellExecutor) {
        return getVariable(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
