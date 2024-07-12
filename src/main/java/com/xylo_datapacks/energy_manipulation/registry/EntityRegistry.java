package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {
    public static final EntityType<SpellEntity> SPELL = Registry.register(Registries.ENTITY_TYPE, 
            EnergyManipulation.id("spell"),
            FabricEntityTypeBuilder.<SpellEntity>create(SpawnGroup.MISC, SpellEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static final EntityType<ProjectileShapeEntity> PROJECTILE_SHAPE = Registry.register(Registries.ENTITY_TYPE,
            EnergyManipulation.id("projectile_shape"),
            FabricEntityTypeBuilder.<ProjectileShapeEntity>create(SpawnGroup.MISC, ProjectileShapeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeBlocks(10).trackedUpdateRate(1).build());
    
    
    public static void init() {
        EnergyManipulation.LOGGER.info("Registering Mod Entities for " + EnergyManipulation.MOD_ID);
    }
}
