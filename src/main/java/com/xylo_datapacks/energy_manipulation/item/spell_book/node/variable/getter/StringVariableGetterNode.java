package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class StringVariableGetterNode extends AbstractVariableGetterNode<String> implements StringNode {
    
    public StringVariableGetterNode() {
        super(Nodes.VARIABLE_GETTER_STRING);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractVariableGetterNode Interface */

    @Override
    public String getVariable(SpellExecutor spellExecutor) {
        String variableName = this.variableName.getNode().getString(spellExecutor);
        if (spellExecutor.getVariable(variableName) instanceof String var) {
            return var;
        }
        return null;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* StringNode Interface */

    @Override
    public String  getString(SpellExecutor spellExecutor) {
        return getVariable(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
