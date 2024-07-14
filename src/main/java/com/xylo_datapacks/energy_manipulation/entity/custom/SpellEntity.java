package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.registry.ModEntityRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellEntity extends AbstractSpellEntity {
    
    public SpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(ModEntityRegistry.SPELL, world);
        this.setNoGravity(true);
    }

    public SpellEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityRegistry.SPELL, x, y, z, world, stack, weapon);
        this.setNoGravity(true);
    }

    public SpellEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityRegistry.SPELL, owner, world, stack, shotFrom);
        this.setNoGravity(true);
    }
    
    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }
    
}
