package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.position;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.ValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.boolean_value.BooleanNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AlignPositionNode extends AbstractNodeWithMap implements PositionNode {
    SubNode<BooleanNode> x = registerSubNode("x", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN))
            .build(this));
    SubNode<BooleanNode> y = registerSubNode("y", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN))
            .build(this));
    SubNode<BooleanNode> z = registerSubNode("z", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN))
            .build(this));
    
    public AlignPositionNode() {
        super(Nodes.POSITION_ALIGN);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* PositionNode Interface */

    @Override
    public Vec3d getPosition(SpellExecutor spellExecutor, Vec3d initialPosition) {
        System.out.println("INITIAL x: " + initialPosition.x + ", y: " + initialPosition.y + ", z: " + initialPosition.z);
        double alignX = x.getNode().getBoolean(spellExecutor) ? Math.floor(initialPosition.x) : initialPosition.x;
        double alignY = y.getNode().getBoolean(spellExecutor) ? Math.floor(initialPosition.y) : initialPosition.y;
        double alignZ = z.getNode().getBoolean(spellExecutor) ? Math.floor(initialPosition.z) : initialPosition.z;
        System.out.println("POST x: " + alignX + ", y: " + alignY + ", z: " + alignZ);
        return new Vec3d(alignX, alignY, alignZ);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
