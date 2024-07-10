package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.shape;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.effect.EffectProviderNode;

import java.util.List;


public class RayShapeNode extends AbstractNodeWithMap implements ShapeNode {
    SubNode<EffectProviderNode> effects = registerSubNode("effects", new SubNode.Builder<EffectProviderNode>()
            .addNodeValues(List.of(
                    Nodes.EFFECT_PROVIDER))
            .build(this));
    
    public RayShapeNode() {
        super(Nodes.SHAPE_RAY);
    }

/*--------------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean generateShape(SpellExecutor spellExecutor) {
        System.out.println("Generating ray shape");
        effects.getNode().executeEffects(spellExecutor, null);
        return true;
    }

/*--------------------------------------------------------------------------------------------------------------------*/
    
}
