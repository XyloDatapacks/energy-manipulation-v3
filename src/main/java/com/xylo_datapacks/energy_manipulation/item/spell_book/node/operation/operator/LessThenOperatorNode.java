package com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractEmptyNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class LessThenOperatorNode extends AbstractEmptyNode implements IntegerOperatorNode, DoubleOperatorNode {

    public LessThenOperatorNode() {
        super(Nodes.OPERATOR_LESS_THEN);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* IntegerOperatorNode Interface */

    @Override
    public Integer applyIntegerOperator(SpellExecutor spellExecutor, Integer a, Integer b) {
        return Math.min(a, b);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* DoubleOperatorNode Interface */

    @Override
    public Double applyDoubleOperator(SpellExecutor spellExecutor, Double a, Double b) {
        return Math.min(a, b);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
