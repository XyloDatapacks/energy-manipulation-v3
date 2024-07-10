package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.effect;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.Registries;

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
