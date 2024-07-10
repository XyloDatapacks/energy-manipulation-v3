package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.effect;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;
import net.minecraft.entity.Entity;

public interface EffectNode extends GenericNode {
    
    public abstract void executeEffect(SpellExecutor spellExecutor, Entity target);
}
