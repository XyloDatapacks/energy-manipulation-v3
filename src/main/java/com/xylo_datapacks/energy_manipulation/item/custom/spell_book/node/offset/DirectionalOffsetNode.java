package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.offset;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.number.NumberNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DirectionalOffsetNode extends AbstractNodeWithMap implements OffsetNode {
    SubNode<NumberNode> x = registerSubNode("x", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.NUMBER_DOUBLE,
                    Nodes.VALUE_TYPE_DOUBLE))
            .build(this));
    SubNode<NumberNode> y = registerSubNode("y", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.NUMBER_DOUBLE,
                    Nodes.VALUE_TYPE_DOUBLE))
            .build(this));
    SubNode<NumberNode> z = registerSubNode("z", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.NUMBER_DOUBLE,
                    Nodes.VALUE_TYPE_DOUBLE))
            .build(this));
    
    public DirectionalOffsetNode() {
        super(Nodes.OFFSET_DIRECTIONAL);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* OffsetNode Interface */

    @Override
    public Vec3d getOffset(SpellExecutor spellExecutor) {
        System.out.println("DirectionalOffsetNode getOffset");
        double offsetX = (double) x.getNode().getNumber(spellExecutor);
        double offsetY = (double) y.getNode().getNumber(spellExecutor);
        double offsetZ = (double) z.getNode().getNumber(spellExecutor);
        return new Vec3d(offsetX, offsetY, offsetZ);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
