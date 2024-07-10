package com.xylo_datapacks.energy_manipulation.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public abstract class AbstractShapeEntity extends DisplayEntity.ItemDisplayEntity {


    public AbstractShapeEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
    
    
    
    
}
