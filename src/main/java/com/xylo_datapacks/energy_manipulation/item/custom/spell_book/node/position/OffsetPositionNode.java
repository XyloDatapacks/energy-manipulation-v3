package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.position;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.offset.OffsetNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class OffsetPositionNode extends AbstractNodeWithMap implements PositionNode{
    SubNode<OffsetNode> offset = registerSubNode("offset", new SubNode.Builder<OffsetNode>()
            .addNodeValues(List.of(
                    Nodes.OFFSET_CARDINAL,
                    Nodes.OFFSET_DIRECTIONAL))
            .build(this));
    
    public OffsetPositionNode() {
        super(Nodes.POSITION_OFFSET);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* PositionNode Interface */

    @Override
    public Vec3d getPosition(SpellExecutor spellExecutor, Vec3d initialPosition) {
        System.out.println("INITIAL x: " + initialPosition.x + ", y: " + initialPosition.y + ", z: " + initialPosition.z);
        Vec3d finalPosition = initialPosition.add(offset.getNode().getOffset(spellExecutor));
        System.out.println("POST x: " + finalPosition.x + ", y: " + finalPosition.y + ", z: " + finalPosition.z);
        return finalPosition;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
