package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class test {
    public static void main(String[] args) {

        Vec3d vec3d = new Vec3d(8, 1, -8);
        vec3d.normalize();
        System.out.println(-MathHelper.atan2(vec3d.x, vec3d.z) * 180.0 / Math.PI);
        
    }
}
