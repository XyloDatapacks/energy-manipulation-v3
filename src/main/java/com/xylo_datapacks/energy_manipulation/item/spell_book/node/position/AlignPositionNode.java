package com.xylo_datapacks.energy_manipulation.item.spell_book.node.position;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import net.minecraft.util.math.Vec3d;

public class AlignPositionNode extends AbstractNodeWithMap implements PositionNode {
    SubNode<BooleanNode> x = registerSubNode("x", SubNodes.CONDITION);
    SubNode<BooleanNode> y = registerSubNode("y", SubNodes.CONDITION);
    SubNode<BooleanNode> z = registerSubNode("z", SubNodes.CONDITION);
    
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
