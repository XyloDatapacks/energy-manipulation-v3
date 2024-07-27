package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.entity.ModEntityType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.AbstractShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ProjectileShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ShapeExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectileShapeEntity extends AbstractShapeEntity {
    private ProjectileShapeNode shapeNode;
    
    public ProjectileShapeEntity(EntityType<? extends ProjectileShapeEntity> entityType, World world) {
        super(ModEntityType.PROJECTILE_SHAPE, world);
    }

    protected ProjectileShapeEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityType.PROJECTILE_SHAPE, x, y, z, world, stack, weapon);
    }

    public ProjectileShapeEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.PROJECTILE_SHAPE, owner, world, stack, shotFrom);
    }

    public ProjectileShapeNode getShapeNode() {
        return shapeNode;
    }

    public void setShapeNode(ProjectileShapeNode shapeNode) {
        this.shapeNode = shapeNode;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* PersistentProjectileEntity Interface */

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        shapeNode.effects.getNode().executeEffects(this, null);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        shapeNode.effects.getNode().executeEffects(this, null);
    }

    /*----------------------------------------------------------------------------------------------------------------*/



    /*----------------------------------------------------------------------------------------------------------------*/
    /* ShapeExecutor Interface */
    
    @Override
    public void setShapeNode(ShapeNode shapeNode) {
        if (shapeNode instanceof ProjectileShapeNode projectileShapeNode) {
            this.shapeNode = projectileShapeNode;
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
