package com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class CardinalOffsetNode extends AbstractNodeWithMap implements OffsetNode {
    SubNode<NumberNode> x = registerSubNode("x", SubNodes.NUMBER);
    SubNode<NumberNode> y = registerSubNode("y", SubNodes.NUMBER);
    SubNode<NumberNode> z = registerSubNode("z", SubNodes.NUMBER);
    
    public CardinalOffsetNode() {
        super(Nodes.OFFSET_CARDINAL);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* OffsetNode Interface */

    @Override
    public Vec3d getOffset(SpellExecutor spellExecutor) {
        double offsetX = x.getNode().getNumber(spellExecutor).doubleValue();
        double offsetY = y.getNode().getNumber(spellExecutor).doubleValue();
        double offsetZ = z.getNode().getNumber(spellExecutor).doubleValue();
        return new Vec3d(offsetX, offsetY, offsetZ);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
