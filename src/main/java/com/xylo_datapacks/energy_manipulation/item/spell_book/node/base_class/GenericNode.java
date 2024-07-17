package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface GenericNode {

    // node Id
    public abstract Identifier getNodeIdentifier();
    
    // nbt
    public abstract NbtCompound toNbt();
    public abstract GenericNode setFromNbt(NbtCompound nbt);
    public static GenericNode generateFromNbt(NbtCompound nbt) {
        return Nodes.NODES.get(Identifier.tryParse(nbt.getString("node_type"))).nodeSupplier().get().setFromNbt(nbt);
    };

    /** get guiData of this node */
    public abstract GuiData getGuiData();
    
    /** get data of this node */
    public static NodeData<? extends GenericNode> getNodeData(Identifier nodeIdentifier) {
        return Nodes.NODES.get(nodeIdentifier);
    };
    /** get data of this node */
    public abstract NodeData<? extends GenericNode> getNodeData();
    /** get data of this sub node */
    public abstract SubNodeData getSubNodeData(String subNodeId);
    
    
    /** get parent node */
    public abstract GenericNode getParentNode();
    /** get nesting */
    public abstract int getNesting();
    
    /** get all sub nodes of this node */
    public abstract Map<String, NodeResult> getAllSubNodes();
    /** get all sub nodes and their sub nodes */
    public abstract Map<String, NodeResult> getAllSubNodesRecursive(List<String> pathStart);
    /** get all sub nodes and their sub nodes */
    public abstract Map<String, NodeResult> getAllSubNodesRecursive(String pathStart);
    /** get all sub nodes and their sub nodes */
    public abstract Map<String, NodeResult> getAllSubNodesRecursive();


    /** get a node result from a path */
    public abstract NodeResult getNodeResultFromPath(List<String> path);
    /** get a node result from a path */
    public abstract NodeResult getNodeResultFromPath(String path);
    
    
    /** get a sub node using a one element path */
    public abstract SubNode<? extends GenericNode> getSubNode(String path);
    /** get a node from a path relative to this node. an empty path returns this node */
    public abstract GenericNode getNodeFromPath(List<String> path);
    /** get a node from a path relative to this node. an empty path returns this node */
    public abstract GenericNode getNodeFromPath(String path);
    
    
    /** modify a sub node using a one element path */
    public abstract boolean modifySubNode(String path, Identifier newSubNodeValueIdentifier);
    /** modify a node from a path relative to this node */
    public abstract boolean modifyNodeFromPath(List<String> path, Identifier newSubNodeValueIdentifier);
    public abstract boolean modifyNodeFromPath(String path, Identifier newSubNodeValueIdentifier);


    /** function to run a node from path 
     * </p> override and implement the node specific resume after the super call */
    public abstract void resumeExecution(SpellExecutor spellExecutor, List<String> path);
    /** function to run a node from path, calls the list version of this function */
    public abstract void resumeExecution(SpellExecutor spellExecutor, String path);
    
    
    
    
    /** split a sting path into a list path */
    public static List<String> stringPathToListPath(String path) {
        return new ArrayList<String>(!path.isBlank() ? List.of(path.split("\\.")) : List.of());
    };
    /** join a list path into a string path */
    public static String listPathToStringPath(List<String> path) {
        return String.join(".", path);
    };
    /** extract the index of a path element, which is in between square brackets "something[index]" */
    public static int stripIndexFromPathElement(String pathElement) {
        int indexStart = pathElement.indexOf("[") + 1;
        int indexEnd = pathElement.length() - 1;
        return Integer.parseInt(pathElement.substring(indexStart, indexEnd));
    }
    static String getSubNodeIdFromPathElement(String pathElement) {
        int indexStart = pathElement.indexOf("[");
        if (indexStart == -1) return pathElement;
        return pathElement.substring(0, indexStart);
    };
}
