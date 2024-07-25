package com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

public class ComplexStringNode extends AbstractNodeWithMap implements StringNode {
    SubNode<StringNode> string = registerSubNode("string", new SubNode.Builder<StringNode>()
                    .addNodeValues(SubNodes.TEMPLATE_ALL_STRINGS),
            Nodes.VALUE_TYPE_STRING.identifier());

    public ComplexStringNode() {
        super(Nodes.STRING);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public String getString(SpellExecutor spellExecutor) {
        return string.getNode().getString(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}