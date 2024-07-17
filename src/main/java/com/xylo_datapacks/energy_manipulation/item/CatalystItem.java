package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.component.CatalystComponent;
import com.xylo_datapacks.energy_manipulation.component.ModDataComponentTypes;
import com.xylo_datapacks.energy_manipulation.component.type.CatalystComponents;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellAttributes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

public class CatalystItem extends Item {
    
    public CatalystItem(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createSpell(World world, ItemStack projectileStack, LivingEntity shooter, ItemStack spellBookStack) {
        SpellNode spellNode = SpellBookItem.getSpellNode(shooter, spellBookStack);
        ProjectileShapeEntity spell = new ProjectileShapeEntity(shooter, world, projectileStack, spellBookStack);
        
        // pass spell
        spell.setSpellNode(spellNode);
        // pass catalyst attributes
        CatalystComponent catalystComponents = projectileStack.get(ModDataComponentTypes.CATALYST);
        if (catalystComponents != null) {
            spell.getSpellData().spellAttributes = new SpellAttributes(catalystComponents);
        }
        // set display
        spell.setDisplayedItemStack(new ItemStack(Items.LECTERN));
        
        return spell;
    }
}
