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
    
    public SpellEntity(EntityType<? extends SpellEntity> entityType, World world) {
        super(ModEntityRegistry.SPELL, world);
    }

    public SpellEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityRegistry.SPELL, x, y, z, world, stack, weapon);
    }

    public SpellEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityRegistry.SPELL, owner, world, stack, shotFrom);
    }
}
