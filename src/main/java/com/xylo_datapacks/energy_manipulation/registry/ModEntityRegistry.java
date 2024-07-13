package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEntityRegistry {
    public static final EntityType<SpellEntity> SPELL = Registry.register(Registries.ENTITY_TYPE, 
            EnergyManipulation.id("spell"),
            EntityType.Builder.<SpellEntity>create(SpellEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build());

    public static final EntityType<ProjectileShapeEntity> PROJECTILE_SHAPE = Registry.register(Registries.ENTITY_TYPE,
            EnergyManipulation.id("projectile_shape"),
            EntityType.Builder.<ProjectileShapeEntity>create(ProjectileShapeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(1).build());
    
    
    public static void init() {
        EnergyManipulation.LOGGER.info("Registering Mod Entities for " + EnergyManipulation.MOD_ID);
    }
}
