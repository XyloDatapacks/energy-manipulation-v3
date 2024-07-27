package com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape;

import com.xylo_datapacks.energy_manipulation.entity.ModEntityType;
import com.xylo_datapacks.energy_manipulation.entity.custom.AbstractSpellEntity;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect.EffectProviderNode;
import net.minecraft.entity.Entity;

import java.util.List;


public class ProjectileShapeNode extends AbstractShapeNode {
    public SubNode<EffectProviderNode> effects = registerSubNode("effects", new SubNode.Builder<EffectProviderNode>()
            .addNodeValues(List.of(
                    Nodes.EFFECT_PROVIDER))
    );
    
    public ProjectileShapeNode() {
        super(Nodes.SHAPE_PROJECTILE);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractShapeNode Interface */

    @Override
    public void summonShape(SpellExecutor spellExecutor) {
        ProjectileShapeEntity projectileShapeEntity = ModEntityType.PROJECTILE_SHAPE.create(spellExecutor.getCaster().getWorld());
        if (projectileShapeEntity != null && spellExecutor instanceof AbstractSpellEntity spellEntity) {
            projectileShapeEntity.copyFrom(spellEntity); //TODO: fix
            projectileShapeEntity.setShapeNode(this);
            ((AbstractSpellEntity) spellExecutor).getWorld().spawnEntity(projectileShapeEntity);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
