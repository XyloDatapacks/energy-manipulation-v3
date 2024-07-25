package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;

public class ComplexDoubleNumberNode extends AbstractNodeWithMap implements NumberNode, DoubleNumberNode {
    SubNode<DoubleNumberNode> value = registerSubNode("value", new SubNode.Builder<DoubleNumberNode>()
                    .addNodeValues(SubNodes.TEMPLATE_ALL_DOUBLE),
            Nodes.VALUE_TYPE_INTEGER.identifier()
    );
    
    public ComplexDoubleNumberNode() {
        super(Nodes.NUMBER_DOUBLE);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* DoubleNumberNode Interface */

    @Override
    public Double getDoubleNumber(SpellExecutor spellExecutor) {
        return value.getNode().getDoubleNumber(spellExecutor);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
}
