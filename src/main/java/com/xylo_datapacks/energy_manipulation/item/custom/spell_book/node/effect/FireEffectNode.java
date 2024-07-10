package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.effect;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class FireEffectNode extends AbstractNodeWithMap implements EffectNode {
    
    public FireEffectNode() {
        super(Nodes.EFFECT_FIRE);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* EffectNode Interface */

    @Override
    public void executeEffect(SpellExecutor spellExecutor, Entity target) {
        if (target != null) {
            System.out.println("fire " + target.getDisplayName() + " at " + target.getPos());
        }
        System.out.println("fire " + " at " + spellExecutor.getContextPosition());
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
