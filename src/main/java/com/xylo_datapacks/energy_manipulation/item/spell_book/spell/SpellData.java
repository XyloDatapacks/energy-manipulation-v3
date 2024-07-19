package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.FromNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.ToNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public class SpellData {
    public static final String CASTER_KEY = "caster";
    public static final String SPELL_NODE_KEY = "spell_node_execution_data";
    public static final String CONTEXT_KEY = "context";
    public static final String ATTRIBUTES_KEY = "attributes";
    
    public Entity caster;
    public SpellNode spellNode;
    public String lastSpellPath;
    private SpellContext spellContext;
    private SpellAttributes spellAttributes;

    public SpellData(Entity caster, SpellNode spellNode, SpellContext spellContext, SpellAttributes spellAttributes) {
        this.caster = caster;
        this.spellNode = spellNode;
        this.spellContext = spellContext;
        this.spellAttributes = spellAttributes;
    }
    
    
    public SpellContext getSpellContext() {
        if (spellContext == null) {
            spellContext = new SpellContext();
        }
        return spellContext;
    }

    public SpellAttributes getSpellAttributes() {
        if (spellAttributes == null) {
            spellAttributes = new SpellAttributes();
        }
        return spellAttributes;
    }
    

    /** if not present in nbt: 
     *      caster, spellNode, spellContext and spellAttributes are null, 
     *      lastSpellPath is empty. 
     *      @param rawSpellNode the spell node from the spell book, without execution data */
    public static SpellData readFromNbt(NbtCompound nbt, World world, SpellNode rawSpellNode) {
        // caster
        Entity caster;
        if (nbt.containsUuid(CASTER_KEY) && world instanceof ServerWorld serverWorld) {
            UUID casterUUID = nbt.getUuid(CASTER_KEY);
            caster = serverWorld.getEntity(casterUUID);
        }
        else {
            caster = null;
        }

        // spell node
        if (rawSpellNode != null) {
            NbtCompound spellExecutionNbt = nbt.getCompound(SPELL_NODE_KEY);
            if (!spellExecutionNbt.isEmpty()) rawSpellNode.setFromNbt(spellExecutionNbt, FromNbtSettings.SET_EXECUTION_DATA);
            
            System.out.println("BUILT SPELL" + rawSpellNode.toNbt(ToNbtSettings.EXECUTION)); //TODO: remove print
        }
        
        
        // spell context
        NbtCompound spellContextNbt = nbt.getCompound(CONTEXT_KEY);
        SpellContext spellContext = !spellContextNbt.isEmpty() ? SpellContext.readFromNbt(spellContextNbt) : null;

        // spell attributes
        NbtCompound spellAttributesNbt = nbt.getCompound(ATTRIBUTES_KEY);
        SpellAttributes spellAttributes = !spellAttributesNbt.isEmpty() ? SpellAttributes.readFromNbt(spellAttributesNbt) : null;

        return new SpellData(caster, rawSpellNode, spellContext, spellAttributes);
    }

    /** write caster, spell_node if not null, 
     * and spell_context, spell_attributes, lastSpellPath only if their compound is not empty */
    public static NbtCompound writeToNbt(SpellData data) {
        NbtCompound spellDataNbt = new NbtCompound();
        if (data == null) return spellDataNbt;

        // caster
        if (data.caster != null) {
            spellDataNbt.putUuid(CASTER_KEY, data.caster.getUuid());
        }

        // spell node
        if (data.spellNode != null) {
            spellDataNbt.put(SPELL_NODE_KEY, data.spellNode.toNbt(ToNbtSettings.EXECUTION_ONLY));
            
            System.out.println("SAVED SPELL" + spellDataNbt.getCompound(SPELL_NODE_KEY)); //TODO: remove print
        }

        // spell context
        NbtCompound spellContext = SpellContext.writeToNbt(data.spellContext);
        if (!spellContext.isEmpty()) {
            spellDataNbt.put(CONTEXT_KEY, spellContext);
        }

        // spell attributes
        NbtCompound spellAttributes = SpellAttributes.writeToNbt(data.spellAttributes);
        if (!spellAttributes.isEmpty()) {
            spellDataNbt.put(ATTRIBUTES_KEY, spellAttributes);
        }
        
        return spellDataNbt;
    }

}