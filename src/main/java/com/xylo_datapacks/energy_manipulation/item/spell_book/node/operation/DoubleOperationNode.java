package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.DoubleOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class DoubleOperationNode extends AbstractNodeWithMap implements NumberOperationNode, DoubleNumberNode {
    SubNode<DoubleNumberNode> number1 = registerSubNode("number1", SubNodes.DOUBLE);
    SubNode<DoubleOperatorNode> operator = registerSubNode("operator", SubNodes.DOUBLE_NUMBER_OPERATOR);
    SubNode<DoubleNumberNode> number2 = registerSubNode("number2", SubNodes.DOUBLE);
    
    public DoubleOperationNode() {
        super(Nodes.OPERATION_DOUBLE_NUMBER);
    }

    public Double performDoubleOperation(SpellExecutor spellExecutor) {
        return operator.getNode().applyDoubleOperator(spellExecutor, number1.getNode().getDoubleNumber(spellExecutor), number2.getNode().getDoubleNumber(spellExecutor));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberOperationNode Interface */

    @Override
    public Number performNumberOperation(SpellExecutor spellExecutor) {
        return performDoubleOperation(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* DoubleNumberNode Interface */

    @Override
    public Double getDoubleNumber(SpellExecutor spellExecutor) {
        return performDoubleOperation(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
