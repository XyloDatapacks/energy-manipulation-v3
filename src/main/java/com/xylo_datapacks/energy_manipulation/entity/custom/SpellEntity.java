package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.xylo_datapacks.energy_manipulation.entity.ModEntityType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SpellEntity extends AbstractSpellEntity {
    
    public SpellEntity(EntityType<? extends SpellEntity> entityType, World world) {
        super(ModEntityType.SPELL, world);
        setBehaveAsProjectile(false);
    }

    public SpellEntity(double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(ModEntityType.SPELL, x, y, z, world, stack, weapon);
        setBehaveAsProjectile(false);
    }

    public SpellEntity(LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.SPELL, owner, world, stack, shotFrom);
        setBehaveAsProjectile(false);
    }
    

    public void runSpell() {
        if (getSpellData().spellNode != null) {
            System.out.println("Executing spell");
            getExecutionData().returnType = ReturnType.NONE;
            getSpellData().spellNode.executeSpell(this);
            
            // spell completely run
            if (getExecutionData().returnType == ReturnType.NONE) {
                this.discard();
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* PersistentProjectileEntity Interface */
    
    @Override
    public void tick() {
        super.tick();
        
        if (decrementSpellDelay(1)) {
            runSpell();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
