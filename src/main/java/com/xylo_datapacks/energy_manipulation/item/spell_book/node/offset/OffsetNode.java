package com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import net.minecraft.util.math.Vec3d;

public interface OffsetNode extends GenericNode {
    
    public abstract Vec3d getOffset(SpellExecutor spellExecutor);
}
