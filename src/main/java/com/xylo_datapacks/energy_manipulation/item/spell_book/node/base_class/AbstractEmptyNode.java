package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.FromNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public abstract class AbstractEmptyNode extends AbstractNode {
    
    public AbstractEmptyNode(NodeData<?> nodeData) {
        super(nodeData);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* GenericNode Interface */
    
    @Override
    public GenericNode setFromNbt(NbtCompound nbt, FromNbtSettings settings) {
        // set guiData
        getGuiData().setFromNbt(nbt.getCompound("gui_data"));
        
        return this;
    }
    
    @Override
    public Map<String, NodeResult> getAllSubNodes() {
        return Map.of();
    }
    
    @Override
    public Map<String, NodeResult> getAllSubNodesRecursive(List<String> pathStart) {
        return Map.of();
    }
    
    @Override
    public SubNode<? extends GenericNode> getSubNode(String path) {
        return null;
    }
    
    @Override
    public boolean modifySubNode(String path, Identifier newSubNodeValueIdentifier) {
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
