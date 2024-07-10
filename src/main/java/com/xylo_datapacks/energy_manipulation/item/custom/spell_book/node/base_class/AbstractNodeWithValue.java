package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public abstract class AbstractNodeWithValue<T> extends AbstractNode implements ValueTypeNode<T> {
    private T value;
    
    public AbstractNodeWithValue(NodeData nodeData) {
        super(nodeData);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* ValueTypeNode Interface */
    
    @Override
    public T getValue() { return value; }

    @Override
    public abstract String getValueDisplay();
    
    @Override
    public boolean setValue(T value) {
        this.value = value;
        return true;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* GenericNode Interface */

    /**
     *  {
     *      node_type: "<@node_identifier>",
     *      gui_data: {...},
     *      value: <@value>
     *  }
     */
    @Override
    public final NbtCompound toNbt() {
        // get base nbt compound
        NbtCompound nbt = super.toNbt();
        
        // add value to nbt
        if (value instanceof Integer intValue) {
            nbt.putInt("value", intValue);
        }
        else if (value instanceof Double doubleValue) {
            nbt.putDouble("value", doubleValue);
        }
        else if (value instanceof Float floatValue) {
            nbt.putFloat("value", floatValue);
        }
        else if (value instanceof Boolean boolValue) {
            nbt.putBoolean("value", boolValue);
        }
        else if (value instanceof String stringValue) {
            nbt.putString("value", stringValue);
        }
        else {
            nbt.putString("value", "failed");
        }
        
        return nbt;
    }

    @Override
    public GenericNode setFromNbt(NbtCompound nbt) {
        // set guiData
        getGuiData().setFromNbt(nbt.getCompound("gui_data"));
        // set value
        if (value instanceof Integer intValue) {
            intValue = nbt.getInt("value");
            value = (T) intValue;
        }
        else if (value instanceof Double doubleValue) {
            doubleValue = nbt.getDouble("value");
            value = (T) doubleValue;
        }
        else if (value instanceof Float floatValue) {
            floatValue = nbt.getFloat("value");
            value = (T) floatValue;
        }
        else if (value instanceof Boolean boolValue) {
            boolValue = nbt.getBoolean("value");
            value = (T) boolValue;
        }
        else if (value instanceof String stringValue) {
            stringValue = nbt.getString("value");
            value = (T) stringValue;
        }
        else {
            value = null;
        }
        
        return this;
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodes() {
        return Map.of();
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodesRecursive(List<String> pathStart) {
        return Map.of();
    }

    @Override
    public final SubNode<? extends GenericNode> getSubNode(String path) {
        return null;
    }

    @Override
    public final boolean modifySubNode(String path, Identifier newSubNodeValueIdentifier) {
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
