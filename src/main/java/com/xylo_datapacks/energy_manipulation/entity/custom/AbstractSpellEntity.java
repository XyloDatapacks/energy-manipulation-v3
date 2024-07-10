package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.spell.SpellNode;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractSpellEntity extends PersistentProjectileEntity implements SpellExecutor {
    protected static final String SPELL_KEY = "spell_data";
    protected static final String CONTEXT_KEY = "context";
    protected static final String POSITION_KEY = "position";
    protected static final String ROTATION_KEY = "rotation";
    private NbtCompound spellData = new NbtCompound();
    private SpellNode spellNode;

    protected AbstractSpellEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    protected AbstractSpellEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    protected AbstractSpellEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* PersistentProjectileEntity Interface */
    
    @Override
    protected ItemStack asItemStack() {
        return null;
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.spellData = nbt.getCompound(SPELL_KEY);
    }
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put(SPELL_KEY, this.spellData.copy());
    }


    /*----------------------------------------------------------------------------------------------------------------*/


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void runSpell() {
        spellNode.executeSpell(this);
    }

    public void setSpellNode(SpellNode spellNode) {
        this.spellNode = spellNode;
    }
    
    protected SpellNode getSpellNode() {
        return this.spellNode;
    }

    protected NbtCompound getSpellData() {
        return this.spellData;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* SpellExecutor Interface */

    @Override
    public LivingEntity getCaster() {
        return (LivingEntity) getOwner();
    }

    @Override
    public Vec3d getContextPosition() {
        NbtList list = (NbtList) spellData.get(CONTEXT_KEY + "." + POSITION_KEY);
        if (list != null && list.size() == 3) {
            return new Vec3d(list.getDouble(0), list.getDouble(1), list.getDouble(2));
        }
        return getPos();
    }

    @Override
    public Vec2f getContextDirection() {
        NbtList list = (NbtList) spellData.get(CONTEXT_KEY + "." + ROTATION_KEY);
        if (list != null && list.size() == 2) {
            return new Vec2f(list.getFloat(0), list.getFloat(1));
        }
        return new Vec2f(getYaw(), getPitch());
    }

    @Override
    public void setContextPosition(Vec3d position) {
        NbtList list = new NbtList();
        list.add(NbtDouble.of(position.x));
        list.add(NbtDouble.of(position.y));
        list.add(NbtDouble.of(position.z));
        spellData.put(CONTEXT_KEY + "." + POSITION_KEY, list);
    }

    @Override
    public void setContextDirection(Vec2f position) {
        NbtList list = new NbtList();
        list.add(NbtDouble.of(position.x));
        list.add(NbtDouble.of(position.y));
        spellData.put(CONTEXT_KEY + "." + ROTATION_KEY, list);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}