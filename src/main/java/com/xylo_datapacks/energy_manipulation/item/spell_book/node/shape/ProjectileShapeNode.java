package com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape;

import com.xylo_datapacks.energy_manipulation.entity.custom.AbstractSpellEntity;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.ToNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect.EffectProviderNode;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;

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
        if (spellExecutor instanceof AbstractSpellEntity spellEntity) {
            ProjectileShapeEntity projectileShapeEntity = new ProjectileShapeEntity(
                    spellExecutor.getContextPosition().x,
                    spellExecutor.getContextPosition().y,
                    spellExecutor.getContextPosition().z,
                    spellEntity.getWorld(),
                    spellEntity.getItemStack(),
                    spellEntity.getWeaponStack()
                    );
            projectileShapeEntity.setOwner(spellEntity.getOwner());
            projectileShapeEntity.setSpellData(spellEntity.getSpellData().copy(spellEntity.getWorld()));
            projectileShapeEntity.refreshShapeNode();
            
            /* set velocity */
            Vec3d direction = Vec3d.fromPolar(spellExecutor.getContextRotation());
            projectileShapeEntity.setVelocity(direction.x, direction.y, direction.z, 2, 0);

            // set display
            projectileShapeEntity.setDisplayedItemStack(new ItemStack(Items.FURNACE));
            
            ((AbstractSpellEntity) spellExecutor).getWorld().spawnEntity(projectileShapeEntity);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
