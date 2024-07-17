package com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import net.minecraft.entity.Entity;

public class BreakEffectNode extends AbstractNodeWithMap implements EffectNode {
    
    public BreakEffectNode() {
        super(Nodes.EFFECT_BREAK);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* EffectNode Interface */

    @Override
    public void executeEffect(SpellExecutor spellExecutor, Entity target) {
        if (target != null) {
            System.out.println("breaking " + target.getDisplayName() + " at " + target.getPos());
        }
        System.out.println("breaking " + " at " + spellExecutor.getContextPosition());
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
