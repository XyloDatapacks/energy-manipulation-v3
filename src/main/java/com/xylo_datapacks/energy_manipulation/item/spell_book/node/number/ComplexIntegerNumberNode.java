package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class ComplexIntegerNumberNode extends AbstractNodeWithMap implements NumberNode, IntegerNumberNode, StringNode {
    SubNode<IntegerNumberNode> value = registerSubNode("value", new SubNode.Builder<IntegerNumberNode>()
                    .addNodeValues(SubNodes.TEMPLATE_ALL_INTEGER),
            Nodes.VALUE_TYPE_INTEGER.identifier());
    
    public ComplexIntegerNumberNode() {
        super(Nodes.NUMBER_INTEGER);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberNode Interface */

    @Override
    public Integer getIntegerNumber(SpellExecutor spellExecutor) {
        return value.getNode().getIntegerNumber(spellExecutor);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* StringNode Interface */

    @Override
    public String getString(SpellExecutor spellExecutor) {
        return String.valueOf(getIntegerNumber(spellExecutor));
    }

    /*----------------------------------------------------------------------------------------------------------------*/


}
