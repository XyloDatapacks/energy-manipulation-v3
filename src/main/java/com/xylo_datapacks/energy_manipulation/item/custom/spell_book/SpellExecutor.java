package com.xylo_datapacks.energy_manipulation.item.custom.spell_book;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public interface SpellExecutor {
    
    public abstract LivingEntity getCaster();

    public abstract Vec3d getContextPosition();
    
    public abstract Vec2f getContextDirection();

    public abstract void setContextPosition(Vec3d position);
    
    public abstract void setContextDirection(Vec2f position);
}
