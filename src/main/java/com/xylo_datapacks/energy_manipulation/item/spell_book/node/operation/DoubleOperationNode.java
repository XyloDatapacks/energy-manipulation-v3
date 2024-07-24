package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.DoubleOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class DoubleOperationNode extends AbstractNodeWithMap implements OperationNode {
    SubNode<StringNode> variable = registerSubNode("variable", SubNodes.STRING);
    SubNode<DoubleOperatorNode> operator = registerSubNode("operator", SubNodes.DOUBLE_NUMBER_OPERATOR);
    SubNode<DoubleNumberNode> number = registerSubNode("number", SubNodes.DOUBLE);
    
    public DoubleOperationNode() {
        super(Nodes.OPERATION_DOUBLE_NUMBER);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* OperationNode Interface */

    @Override
    public void performOperation(SpellExecutor spellExecutor) {
        String variableName = variable.getNode().getString();
        if (spellExecutor.getVariable(variableName) instanceof Double doubleVariable) {
            Double result = operator.getNode().applyDoubleOperator(spellExecutor, doubleVariable, number.getNode().getDoubleNumber(spellExecutor));
            spellExecutor.setVariable(variableName, result);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/


}
