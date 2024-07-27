package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.VariableType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import javax.xml.transform.Result;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

public class SpellContext {
    public static final String POSITION_KEY = "position";
    public static final String ROTATION_KEY = "rotation";
    public static final String VARIABLES_KEY = "variables";
    
    private Vec3d position;
    private Vec2f rotation;
    private final Map<String, Object> variables = new HashMap<>();
    
    
    
    
    public SpellContext(Vec3d position, Vec2f rotation, Map<String, Object> variables) {
        this.position = position;
        this.rotation = rotation;
        this.variables.putAll(variables);
    }
    
    public SpellContext(Entity entity) {
        this(entity.getPos(), new Vec2f(entity.getYaw(), entity.getPitch()), new HashMap<>());
    }

    public SpellContext() {
    }
    
    

    
    public Vec3d getPosition() {
        return position;
    }
    
    public Vec2f getRotation() {
        return rotation;
    }
    
    public void setPosition(Vec3d position) {
        this.position = position;
    }
    
    public void setRotation(Vec2f rotation) {
        this.rotation = rotation;
    }
    
    
    
    
    public boolean createVariable(String variableName, VariableType variableType) {
        if (variables.containsKey(variableName)) return false;
        
        variables.put(variableName, variableType.CreateVariable());
        return true;
    }

    public boolean setVariable(String variableName, Object result) {
        if (!variables.containsKey(variableName)) return false;
        if (!variables.get(variableName).getClass().isInstance(result)) return false;
        
        variables.put(variableName, result);
        return true;
    }
    
    public Object getVariable(String variableName) {
        return variables.get(variableName);
    }
    
    
    
    
    /** position and rotation are null if not present in nbt */
    public static SpellContext readFromNbt(NbtCompound nbt) {
        // position
        NbtList positionNbt = nbt.getList(POSITION_KEY, NbtElement.DOUBLE_TYPE);
        Vec3d position = positionNbt.size() == 3 ?
                new Vec3d(positionNbt.getDouble(0), positionNbt.getDouble(1), positionNbt.getDouble(2))
                : null;

        // rotation
        NbtList rotationNbt = nbt.getList(ROTATION_KEY, NbtElement.FLOAT_TYPE);
        Vec2f rotation = rotationNbt.size() == 2 ?
                new Vec2f(rotationNbt.getFloat(0), rotationNbt.getFloat(1))
                : null;

        // variables
        NbtCompound variablesNbt = nbt.getCompound(VARIABLES_KEY);
        Map<String, Object> variables = new HashMap<>();
        variablesNbt.getKeys().forEach(key -> {
            NbtElement nbtElement = variablesNbt.get(key);
            variables.put(key, VariableType.readFromNbt(nbtElement));
        });
        
        return new SpellContext(position, rotation, variables);
    }

    /** write position and rotation only if they are not null */
    public static NbtCompound writeToNbt(SpellContext context) {
        NbtCompound spellContextNbt = new NbtCompound();
        if (context == null) return spellContextNbt;
        
        // position
        if (context.position != null) {
            NbtList positionNbt = new NbtList();
            positionNbt.add(NbtDouble.of(context.position.x));
            positionNbt.add(NbtDouble.of(context.position.y));
            positionNbt.add(NbtDouble.of(context.position.z));
            spellContextNbt.put(POSITION_KEY, positionNbt);
        }
        
        // rotation
        if (context.rotation != null) {
            NbtList rotationNbt = new NbtList();
            rotationNbt.add(NbtDouble.of(context.rotation.x));
            rotationNbt.add(NbtDouble.of(context.rotation.y));
            spellContextNbt.put(ROTATION_KEY, rotationNbt);
        }
        
        // variables
        if (!context.variables.isEmpty()) {
            NbtCompound variablesNbt = new NbtCompound();
            for (Map.Entry<String, Object> entry : context.variables.entrySet()) {
                NbtElement var = VariableType.writeToNbt(entry.getValue());
                if (var != null) {
                    variablesNbt.put(entry.getKey(), var);
                }
            }
            spellContextNbt.put(VARIABLES_KEY, variablesNbt);
        }

        return spellContextNbt;
    }
}