package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.entity.ModEntityType;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellEntity extends AbstractSpellEntity {
    
    public SpellEntity(EntityType<? extends SpellEntity> entityType, World world) {
        super(ModEntityType.SPELL, world);
        init();
    }

    public SpellEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityType.SPELL, x, y, z, world, stack, weapon);
        init();
    }

    public SpellEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.SPELL, owner, world, stack, shotFrom);
        init();
    }
    
    private void init() {
        setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();

        if (decrementSpellDelay(1)) {
            runSpell();
        }
    }
}
