package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.registry.EntityRegistry;
import com.xylo_datapacks.energy_manipulation.item.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class SpellEntity extends AbstractSpellEntity {
    
    public SpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public SpellEntity(World world, double x, double y, double z) {
        super(EntityRegistry.SPELL, x, y, z, world);
        this.setNoGravity(true);
    }

    public SpellEntity(World world, LivingEntity owner) {
        super(EntityRegistry.SPELL, owner, world);
        this.setNoGravity(true);
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }
    
}
