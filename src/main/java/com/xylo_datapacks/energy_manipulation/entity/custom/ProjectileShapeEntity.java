package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.registry.ModEntityRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ProjectileShapeEntity extends AbstractShapeEntity {
    
    public ProjectileShapeEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntityRegistry.PROJECTILE_SHAPE, world);
        this.setNoGravity(true);
    }

    protected ProjectileShapeEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityRegistry.PROJECTILE_SHAPE, x, y, z, world, stack, weapon);
        this.setNoGravity(true);
    }

    protected ProjectileShapeEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityRegistry.PROJECTILE_SHAPE, owner, world, stack, shotFrom);
        this.setNoGravity(true);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }
}
