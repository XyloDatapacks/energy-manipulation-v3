package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.IntegerOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class IntegerOperationNode extends AbstractNodeWithMap implements OperationNode {
    SubNode<StringNode> variable = registerSubNode("variable", SubNodes.STRING);
    SubNode<IntegerOperatorNode> operator = registerSubNode("operator", SubNodes.INTEGER_NUMBER_OPERATOR);
    SubNode<IntegerNumberNode> number = registerSubNode("number", SubNodes.INTEGER);
    
    public IntegerOperationNode() {
        super(Nodes.OPERATION_INTEGER_NUMBER);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* OperationNode Interface */

    @Override
    public void performOperation(SpellExecutor spellExecutor) {
        String variableName = variable.getNode().getString();
        if (spellExecutor.getVariable(variableName) instanceof Integer integerVariable) {
            Integer result = operator.getNode().applyIntegerOperator(spellExecutor, integerVariable, number.getNode().getIntegerNumber(spellExecutor));
            spellExecutor.setVariable(variableName, result);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/


}
