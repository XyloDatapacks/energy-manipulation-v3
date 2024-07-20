package com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DirectionalOffsetNode extends AbstractNodeWithMap implements OffsetNode {
    SubNode<NumberNode> x = registerSubNode("x", SubNodes.NUMBER.subNodeBuilderTemplate());
    SubNode<NumberNode> y = registerSubNode("y", SubNodes.NUMBER.subNodeBuilderTemplate());
    SubNode<NumberNode> z = registerSubNode("z", SubNodes.NUMBER.subNodeBuilderTemplate());
    
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
