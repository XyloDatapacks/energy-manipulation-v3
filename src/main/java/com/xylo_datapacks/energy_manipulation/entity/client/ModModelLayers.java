package com.xylo_datapacks.energy_manipulation.entity.client;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer SPELL = new EntityModelLayer(
            EnergyManipulation.id("spell"), "main");

    public static final EntityModelLayer PROJECTILE_SHAPE = new EntityModelLayer(
            EnergyManipulation.id("projectile_shape"), "main");
}
