package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.AbstractShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ShapeExecutor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractShapeEntity extends AbstractSpellEntity implements ShapeExecutor {
    
    protected AbstractShapeEntity(EntityType<? extends AbstractShapeEntity> entityType, World world) {
        super(entityType, world);
    }

    protected AbstractShapeEntity(EntityType<? extends AbstractShapeEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
    }

    protected AbstractShapeEntity(EntityType<? extends AbstractShapeEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* PersistentProjectileEntity Interface */

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        // spell node execution is stopped at the shape node that created this spell. so we run the spell to
        // run the shape node resumeExecution, which will set the shape node of this shape
        getSpellData().spellNode.executeSpell(this);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
