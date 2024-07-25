package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.IntegerOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class IntegerOperationNode extends AbstractNodeWithMap implements NumberOperationNode, IntegerNumberNode {
    SubNode<IntegerNumberNode> number1 = registerSubNode("number1", SubNodes.INTEGER);
    SubNode<IntegerOperatorNode> operator = registerSubNode("operator", SubNodes.INTEGER_NUMBER_OPERATOR);
    SubNode<IntegerNumberNode> number2 = registerSubNode("number2", SubNodes.INTEGER);
    
    public IntegerOperationNode() {
        super(Nodes.OPERATION_INTEGER_NUMBER);
    }

    public Integer performIntegerOperation(SpellExecutor spellExecutor) {
        return operator.getNode().applyIntegerOperator(spellExecutor, number1.getNode().getIntegerNumber(spellExecutor), number2.getNode().getIntegerNumber(spellExecutor));
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberOperationNode Interface */

    @Override
    public Number performNumberOperation(SpellExecutor spellExecutor) {
        return performIntegerOperation(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* IntegerNumberNode Interface */

    @Override
    public Integer getIntegerNumber(SpellExecutor spellExecutor) {
        return performIntegerOperation(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
