package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class SpellContext {
    protected static final String POSITION_KEY = "position";
    protected static final String ROTATION_KEY = "rotation";
    
    private Vec3d position;
    private Vec2f rotation;
    
    public SpellContext(Vec3d position, Vec2f rotation) {
        this.position = position;
        this.rotation = rotation;
    }
    
    public SpellContext(Entity entity) {
        this(entity.getPos(), new Vec2f(entity.getYaw(), entity.getPitch()));
    }

    public SpellContext() {
    }
    
    

    public Vec3d getPosition() {
        return position;
    }
    
    public Vec2f getRotation() {
        return rotation;
    }
    
    public void setPosition(Vec3d position) {
        this.position = position;
    }
    
    public void setRotation(Vec2f rotation) {
        this.rotation = rotation;
    }
    
    
    /** position and rotation are null if not present in nbt */
    public static SpellContext readFromNbt(NbtCompound nbt) {
        // position
        NbtList positionNbt = nbt.getList(POSITION_KEY, NbtElement.DOUBLE_TYPE);
        Vec3d position = positionNbt != null && positionNbt.size() == 3 ?
                new Vec3d(positionNbt.getDouble(0), positionNbt.getDouble(1), positionNbt.getDouble(2))
                : null;

        // rotation
        NbtList rotationNbt = nbt.getList(ROTATION_KEY, NbtElement.FLOAT_TYPE);
        Vec2f rotation = rotationNbt != null && rotationNbt.size() == 2 ?
                new Vec2f(rotationNbt.getFloat(0), rotationNbt.getFloat(1))
                : null;

        return new SpellContext(position, rotation);
    }

    /** write position and rotation only if they are not null */
    public static NbtCompound writeToNbt(SpellContext context) {
        NbtCompound spellContextNbt = new NbtCompound();
        if (context == null) return spellContextNbt;
        
        // position
        if (context.position != null) {
            NbtList positionNbt = new NbtList();
            positionNbt.add(NbtDouble.of(context.position.x));
            positionNbt.add(NbtDouble.of(context.position.y));
            positionNbt.add(NbtDouble.of(context.position.z));
            spellContextNbt.put(POSITION_KEY, positionNbt);
        }
        
        // rotation
        if (context.rotation != null) {
            NbtList rotationNbt = new NbtList();
            rotationNbt.add(NbtDouble.of(context.rotation.x));
            rotationNbt.add(NbtDouble.of(context.rotation.y));
            spellContextNbt.put(ROTATION_KEY, rotationNbt);
        }

        return spellContextNbt;
    }
}