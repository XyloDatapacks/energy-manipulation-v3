package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.position;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;
import net.minecraft.util.math.Vec3d;

public interface PositionNode extends GenericNode {
    
    public abstract Vec3d getPosition(SpellExecutor spellExecutor, Vec3d initialPosition);
    
}
