package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.ValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.List;

public class IntegerNumberNode extends AbstractNodeWithMap implements NumberNode {
    SubNode<ValueTypeNode<Integer>> value = registerSubNode("value", new SubNode.Builder<ValueTypeNode<Integer>>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_INTEGER))
            .build(this));
    
    public IntegerNumberNode() {
        super(Nodes.NUMBER_INTEGER);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberNode Interface */

    @Override
    public Number getNumber(SpellExecutor spellExecutor) {
        return value.getNode().getValue();
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    

}
