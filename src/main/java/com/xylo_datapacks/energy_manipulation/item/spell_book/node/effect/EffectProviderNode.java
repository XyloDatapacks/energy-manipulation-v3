package com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithList;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import net.minecraft.entity.Entity;

import java.util.List;

public class EffectProviderNode extends AbstractNodeWithList<EffectNode> {
    
    public EffectProviderNode() {
        super(Nodes.EFFECT_PROVIDER, "effect", new SubNode.Builder<EffectNode>()
                .addNodeValues(List.of(
                        Nodes.EFFECT_BREAK,
                        Nodes.EFFECT_FIRE)
                ), Nodes.EFFECT_FIRE.identifier());
    }
    
    public void executeEffects(SpellExecutor spellExecutor, Entity target) {
        getSubNodes().forEach(subNode -> {
            subNode.getNode().executeEffect(spellExecutor, target);
        });
    }
}
