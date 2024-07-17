package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellAttributes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractSpellEntity extends AbstractDisplayProjectile.AbstractItemDisplayProjectile implements SpellExecutor {
    public static final String SPELL_DATA_KEY = "spell_data";
    private SpellData spellData;

    public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, World world) {
        super(entityType, world);
        spellData = new SpellData(getOwner(), null, null, null);
    }

    protected AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
        spellData = new SpellData(getOwner(), null, null, null);
    }

    protected AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
        spellData = new SpellData(getOwner(), null, null, null);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* PersistentProjectileEntity Interface */

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        
        this.spellData = SpellData.readFromNbt(nbt, this.getWorld());
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        
        nbt.put(SPELL_DATA_KEY, SpellData.writeToNbt(this.spellData));
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ENCHANTED_BOOK);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    public void runSpell() {
        if (spellData.spellNode != null) {
            spellData.spellNode.executeSpell(this);
        }
    }

    public void setSpellNode(SpellNode spellNode) {
        spellData.spellNode = spellNode;
    }
    
    protected SpellNode getSpellNode() {
        return spellData.spellNode;
    }
    
    public SpellData getSpellData() {
        return spellData;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* SpellExecutor Interface */

    @Override
    public Entity getCaster() {
        return Optional.ofNullable(spellData.caster).orElse(getOwner());
    }

    @Override
    public Vec3d getContextPosition() {
        return Optional.ofNullable(spellData.spellContext.getPosition()).orElse(getPos());
    }

    @Override
    public Vec2f getContextRotation() {
        return Optional.ofNullable(spellData.spellContext.getRotation()).orElse(new Vec2f(getYaw(), getPitch()));
    }

    @Override
    public void setContextPosition(Vec3d position) {
        spellData.spellContext.setPosition(position);
    }

    @Override
    public void setContextRotation(Vec2f rotation) {
        spellData.spellContext.setRotation(rotation); 
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    
    
    
}