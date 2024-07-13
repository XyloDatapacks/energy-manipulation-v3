package com.xylo_datapacks.energy_manipulation.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("setRotation")
    public void invokeSetRotation(float yaw, float pitch);
    
    @Invoker("lerpPosAndRotation")
    public void invokeLerpPosAndRotation(int step, double x, double y, double z, double yaw, double pitch);
    
}
