package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public interface SpellExecutor {
    
    public abstract Entity getCaster();

    public abstract Vec3d getContextPosition();
    
    public abstract Vec2f getContextRotation();

    public abstract void setContextPosition(Vec3d position);
    
    public abstract void setContextRotation(Vec2f position);
}
