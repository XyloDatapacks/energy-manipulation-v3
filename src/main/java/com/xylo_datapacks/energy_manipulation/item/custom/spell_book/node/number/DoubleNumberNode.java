package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.ValueTypeNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DoubleNumberNode extends AbstractNodeWithMap implements NumberNode {
    SubNode<ValueTypeNode<Double>> value = registerSubNode("value", new SubNode.Builder<ValueTypeNode<Double>>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_DOUBLE))
            .build(this));
    
    public DoubleNumberNode() {
        super(Nodes.NUMBER_DOUBLE);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberNode Interface */

    @Override
    public Number getNumber(SpellExecutor spellExecutor) {
        return value.getNode().getValue();
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
}
