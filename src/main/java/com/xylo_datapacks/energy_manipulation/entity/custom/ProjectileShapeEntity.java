package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.world.World;

public class ProjectileShapeEntity extends AbstractShapeEntity {


    public ProjectileShapeEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
}
