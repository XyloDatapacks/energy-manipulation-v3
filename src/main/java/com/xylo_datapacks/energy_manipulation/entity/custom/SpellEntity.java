package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.registry.EntityRegistry;
import com.xylo_datapacks.energy_manipulation.item.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellEntity extends AbstractSpellEntity {


    public SpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(EntityRegistry.SPELL, world);
    }

    protected SpellEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(EntityRegistry.SPELL, x, y, z, world, stack, weapon);
    }

    protected SpellEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityRegistry.SPELL, owner, world, stack, shotFrom);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }
    
}
