package com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class DirectionalOffsetNode extends AbstractNodeWithMap implements OffsetNode {
    SubNode<NumberNode> x = registerSubNode("x", SubNodes.NUMBER);
    SubNode<NumberNode> y = registerSubNode("y", SubNodes.NUMBER);
    SubNode<NumberNode> z = registerSubNode("z", SubNodes.NUMBER);
    
    public DirectionalOffsetNode() {
        super(Nodes.OFFSET_DIRECTIONAL);
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* OffsetNode Interface */

    @Override
    public Vec3d getOffset(SpellExecutor spellExecutor) {
        double offsetX = x.getNode().getNumber(spellExecutor).doubleValue();
        double offsetY = y.getNode().getNumber(spellExecutor).doubleValue();
        double offsetZ = z.getNode().getNumber(spellExecutor).doubleValue();
        
        // stolen from LookingPosArgument#toAbsolutePos
        Vec2f vec2f = new Vec2f(spellExecutor.getContextRotation().y, spellExecutor.getContextRotation().x);
        float f = MathHelper.cos((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float g = MathHelper.sin((vec2f.y + 90.0F) * (float) (Math.PI / 180.0));
        float h = MathHelper.cos(-vec2f.x * (float) (Math.PI / 180.0));
        float i = MathHelper.sin(-vec2f.x * (float) (Math.PI / 180.0));
        float j = MathHelper.cos((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        float k = MathHelper.sin((-vec2f.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3d vec3d2 = new Vec3d((double)(f * h), (double)i, (double)(g * h));
        Vec3d vec3d3 = new Vec3d((double)(f * j), (double)k, (double)(g * j));
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3).multiply(-1.0);
        double d = vec3d2.x * offsetZ + vec3d3.x * offsetY + vec3d4.x * offsetX;
        double e = vec3d2.y * offsetZ + vec3d3.y * offsetY + vec3d4.y * offsetX;
        double l = vec3d2.z * offsetZ + vec3d3.z * offsetY + vec3d4.z * offsetX;
        return new Vec3d(d, e, l);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
