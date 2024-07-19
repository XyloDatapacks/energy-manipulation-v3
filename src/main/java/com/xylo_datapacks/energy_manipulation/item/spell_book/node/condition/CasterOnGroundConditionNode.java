package com.xylo_datapacks.energy_manipulation.item.spell_book.node.condition;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.entity.Entity;

public class CasterOnGroundConditionNode extends AbstractNodeWithMap implements ConditionNode {

    public CasterOnGroundConditionNode() {
        super(Nodes.CONDITION_CASTER_ON_GROUND);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* BooleanNode Interface */

    @Override
    public Boolean getBoolean(SpellExecutor spellExecutor) {
        Entity caster = spellExecutor.getCaster();
        return caster != null && caster.isOnGround();
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
