package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractNode implements GenericNode {
    private final Identifier nodeIdentifier;
    private GenericNode parentNode;
    private int nesting = 0;
    private final GuiData guiData;
    
    public AbstractNode(NodeData<?> nodeData) {
        this.nodeIdentifier = nodeData.identifier();
        guiData = new GuiData();
    }
    
    /** set parent node */
    protected final void setParentNode(GenericNode parent) { 
        parentNode = parent; 
        nesting = parentNode.getNesting() + 1;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* GenericNode Interface */
    
    @Override
    public final Identifier getNodeIdentifier() { return nodeIdentifier; }
    
    
    /**
     *  {
     *      node_type: "<@node_identifier>",
     *      gui_data: {...}
     *  }
     */
    @Override
    public NbtCompound toNbt(ToNbtSettings settings) {
        if (settings.saveOnlyExecutionData()) return new NbtCompound(); // {}
        
        // {}
        NbtCompound nbt = new NbtCompound();
        // add node_type: "<@node_identifier>" to nbt
        nbt.putString("node_type", getNodeIdentifier().toString());
        // add guiData
        if (settings.saveGuiData()) nbt.put("gui_data", getGuiData().toNbt());
        
        return nbt;
    }

    
    
    @Override
    public final GuiData getGuiData() {
        return guiData;
    }

    
    
    @Override
    public final NodeData<? extends GenericNode> getNodeData() {
        return Nodes.NODES.get(nodeIdentifier);
    }

    @Override
    public final SubNodeData getSubNodeData(String subNodeId) {
        return Nodes.NODES.get(nodeIdentifier).subNodes().get(subNodeId);
    }
    
    
    
    @Override
    public final GenericNode getParentNode() { return parentNode; };

    @Override
    public int getNesting() {
        return nesting;
    }

    
    
    @Override
    public final Map<String, NodeResult> getAllSubNodesRecursive(String pathStart) {
        return getAllSubNodesRecursive(GenericNode.stringPathToListPath(pathStart));
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodesRecursive() {
        return getAllSubNodesRecursive(new ArrayList<>());
    }

    
    
    @Override
    public final NodeResult getNodeResultFromPath(List<String> path) {
        if (path.isEmpty()) return null;
        List<String> pathSaved = new ArrayList<>(path);
        return new NodeResult(new NodePath(pathSaved, GenericNode.getSubNodeIdFromPathElement(pathSaved.get(pathSaved.size()-1))), getNodeFromPath(path));
    }

    @Override
    public final NodeResult getNodeResultFromPath(String path) {
        return getNodeResultFromPath(GenericNode.stringPathToListPath(path));
    }
    

    
    @Override
    public final GenericNode getNodeFromPath(List<String> path) {
        if (path.isEmpty()) return this;

        SubNode<? extends GenericNode> node = this.getSubNode(path.get(0));

        if (node != null) {
            path.remove(0);
            return node.getNode().getNodeFromPath(path);
        }
        if (!path.isEmpty()) {
            System.out.println("path failed at: " + path);
        }
        return null;
    }

    @Override
    public final GenericNode getNodeFromPath(String path) {
        return getNodeFromPath(GenericNode.stringPathToListPath(path));
    }

    
    
    @Override
    public final boolean modifyNodeFromPath(List<String> path, Identifier newSubNodeValueIdentifier) {
        if (path.isEmpty()) return false;
        if (path.size() == 1) return modifySubNode(path.get(0), newSubNodeValueIdentifier);

        // we have a compound path. the last path element is the target subNode
        String subNodeTargetPath = path.remove(path.size() - 1);
        // we find the parent of the target subNode (since we removed the last element)
        GenericNode parentNode = this.getNodeFromPath(path);
        if (parentNode != null) {
            // now that we have the parent node, we can call this function again 
            // with the path of the target subNode (size == 1)
            return parentNode.modifyNodeFromPath(subNodeTargetPath, newSubNodeValueIdentifier);
        }
        return false;
    }

    @Override
    public final boolean modifyNodeFromPath(String path, Identifier newSubNodeValueIdentifier) {
        return modifyNodeFromPath(GenericNode.stringPathToListPath(path), newSubNodeValueIdentifier);
    }
    

    /*----------------------------------------------------------------------------------------------------------------*/
    
    
}
