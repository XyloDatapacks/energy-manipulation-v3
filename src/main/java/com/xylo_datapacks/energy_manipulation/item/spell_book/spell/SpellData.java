package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.RunnableNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeResult;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public class SpellData {
    public static final String CASTER_KEY = "caster";
    public static final String SPELL_NODE_KEY = "spell_node";
    public static final String LAST_SPELL_PATH = "last_spell_path";
    public static final String CONTEXT_KEY = "context";
    public static final String ATTRIBUTES_KEY = "attributes";
    
    public Entity caster;
    public SpellNode spellNode;
    public String lastSpellPath;
    private SpellContext spellContext;
    private SpellAttributes spellAttributes;

    public SpellData(Entity caster, SpellNode spellNode, String lastSpellPath, SpellContext spellContext, SpellAttributes spellAttributes) {
        this.caster = caster;
        this.spellNode = spellNode;
        this.lastSpellPath = lastSpellPath;
        this.spellContext = spellContext;
        this.spellAttributes = spellAttributes;
    }
    
    public SpellData(Entity caster, SpellNode spellNode, SpellContext spellContext, SpellAttributes spellAttributes) {
        this(caster, spellNode, "", spellContext, spellAttributes);
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
     *      lastSpellPath is empty. */
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
        
        // last spell path
        String lastSpellPath = nbt.getString(LAST_SPELL_PATH);
        
        // spell context
        NbtCompound spellContextNbt = nbt.getCompound(CONTEXT_KEY);
        SpellContext spellContext = !spellContextNbt.isEmpty() ? SpellContext.readFromNbt(spellContextNbt) : null;

        // spell attributes
        NbtCompound spellAttributesNbt = nbt.getCompound(ATTRIBUTES_KEY);
        SpellAttributes spellAttributes = !spellAttributesNbt.isEmpty() ? SpellAttributes.readFromNbt(spellAttributesNbt) : null;

        return new SpellData(caster, spellNode, lastSpellPath, spellContext, spellAttributes);
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
            spellDataNbt.put(SPELL_NODE_KEY, data.spellNode.toNbt());

            // TODO: Properly implement
            Map<String, NodeResult> map = data.spellNode.getAllSubNodesRecursive();
            NbtCompound nbt = new NbtCompound();
            map.forEach((key, value) -> {
                if (value.node() instanceof RunnableNode<?> runnableNode) {
                    NbtCompound compound = new NbtCompound();
                    runnableNode.saveExecutionToNbt(compound);
                    nbt.put(key, compound);
                }
            });

            System.out.println(nbt);
        }

        // last spell path
        if (data.lastSpellPath != null && !data.lastSpellPath.isEmpty()) {
            spellDataNbt.putString(LAST_SPELL_PATH, data.lastSpellPath);
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