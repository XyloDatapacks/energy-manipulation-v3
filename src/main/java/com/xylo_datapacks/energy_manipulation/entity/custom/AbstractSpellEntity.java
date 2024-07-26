package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.VariableType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.*;
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
    public static final String SPELL_DELAY_KEY = "spell_delay";
    private SpellData spellData;
    private int spellDelay = 0;
    private ExecutionData executionData = new ExecutionData();

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
        
        SpellNode spellNode = this.getWeaponStack() != null && this.getWeaponStack().getItem() instanceof SpellBookItem 
                ? SpellBookItem.getSpellNode(this.getRegistryManager(), this.getWeaponStack()) 
                : null;
        this.spellData = SpellData.readFromNbt(nbt.getCompound(SPELL_DATA_KEY), this.getWorld(), spellNode);
        this.spellDelay = nbt.getInt(SPELL_DELAY_KEY);
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        
        nbt.put(SPELL_DATA_KEY, SpellData.writeToNbt(this.spellData));
        nbt.putInt(SPELL_DELAY_KEY, spellDelay);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ENCHANTED_BOOK);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    public void runSpell() {
        if (spellData.spellNode != null) {
            System.out.println("Executing spell");
            getExecutionData().returnType = ReturnType.NONE;
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
    
    public int getSpellDelay() {
        return spellDelay;
    }
    
    /** decrement spell delay by delta
     * @return true if the delay is done */
    public boolean decrementSpellDelay(int delta) {
        if (spellDelay > 0) {
            spellDelay = Math.max(spellDelay - delta, 0);
            return false;
        }
        return true;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* SpellExecutor Interface */

    @Override
    public void setDelayTicks(int ticks) {
        spellDelay = ticks;
    }

    @Override
    public Entity getCaster() {
        return Optional.ofNullable(spellData.caster).orElse(getOwner());
    }

    @Override
    public Vec3d getContextPosition() {
        return Optional.ofNullable(spellData.getSpellContext().getPosition()).orElse(getPos());
    }

    @Override
    public Vec2f getContextRotation() {
        return Optional.ofNullable(spellData.getSpellContext().getRotation()).orElse(new Vec2f(getYaw(), getPitch()));
    }

    @Override
    public void setContextPosition(Vec3d position) {
        spellData.getSpellContext().setPosition(position);
    }

    @Override
    public void setContextRotation(Vec2f rotation) {
        spellData.getSpellContext().setRotation(rotation); 
    }

    @Override
    public ExecutionData getExecutionData() {
        return executionData;
    }
    
    @Override
    public boolean createVariable(String variableName, VariableType variableType) {
        return spellData.getSpellContext().createVariable(variableName, variableType);
    }

    @Override
    public boolean setVariable(String variableName, Object result) {
        return spellData.getSpellContext().setVariable(variableName, result);
    }

    @Override
    public Object getVariable(String variableName) {
        return spellData.getSpellContext().getVariable(variableName);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    
    
    
}