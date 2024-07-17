package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public class SpellData {
    public static final String CASTER_KEY = "caster";
    public static final String SPELL_NODE_KEY = "spell_node";
    public static final String CONTEXT_KEY = "context";
    public static final String ATTRIBUTES_KEY = "attributes";
    
    public Entity caster;
    public SpellNode spellNode;
    public SpellContext spellContext;
    public SpellAttributes spellAttributes;

    public SpellData(Entity caster, SpellNode spellNode, SpellContext spellContext, SpellAttributes spellAttributes) {
        this.caster = caster;
        this.spellNode = spellNode;
        this.spellContext = spellContext;
        this.spellAttributes = spellAttributes;
    }

    /** caster, spellNode, spellContext and spellAttributes are null if not present in nbt */
    public static SpellData readFromNbt(NbtCompound nbt, World world) {
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
        NbtCompound spellNodeNbt = nbt.getCompound(SPELL_NODE_KEY);
        SpellNode spellNode = !spellNodeNbt.isEmpty() && GenericNode.generateFromNbt(spellNodeNbt) instanceof SpellNode genSpellNode 
                ? genSpellNode 
                : null;
        
        // spell context
        NbtCompound spellContextNbt = nbt.getCompound(CONTEXT_KEY);
        SpellContext spellContext = !spellContextNbt.isEmpty() ? SpellContext.readFromNbt(spellContextNbt) : null;

        // spell attributes
        NbtCompound spellAttributesNbt = nbt.getCompound(ATTRIBUTES_KEY);
        SpellAttributes spellAttributes = !spellAttributesNbt.isEmpty() ? SpellAttributes.readFromNbt(spellAttributesNbt) : null;

        return new SpellData(caster, spellNode, spellContext, spellAttributes);
    }

    /** write caster and spell_node if not null, and spell_context, spell_attributes only if their compound is not empty */
    public static NbtCompound writeToNbt(SpellData data) {
        NbtCompound spellDataNbt = new NbtCompound();
        if (data == null) return spellDataNbt;

        if (data.caster != null) {
            spellDataNbt.putUuid(CASTER_KEY, data.caster.getUuid());
        }
        
        if (data.spellNode != null) {
            spellDataNbt.put(SPELL_NODE_KEY, data.spellNode.toNbt());
        }
        
        NbtCompound spellContext = SpellContext.writeToNbt(data.spellContext);
        if (!spellContext.isEmpty()) {
            spellDataNbt.put(CONTEXT_KEY, spellContext);
        }

        NbtCompound spellAttributes = SpellAttributes.writeToNbt(data.spellAttributes);
        if (!spellAttributes.isEmpty()) {
            spellDataNbt.put(ATTRIBUTES_KEY, spellAttributes);
        }
        
        return spellDataNbt;
    }

}