package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.VariableType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public interface ShapeExecutor extends SpellExecutor {

    public abstract void setShapeNode(ShapeNode shapeNode);
    
}
