package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class AbstractNodeWithMap extends AbstractNode {
    private final Map<String, SubNode<? extends GenericNode>> subNodes = new LinkedHashMap<>();
    
    public AbstractNodeWithMap(NodeData<?> nodeData) {
        super(nodeData);
    }

    protected final <T extends GenericNode> SubNode<T> registerSubNode(String subNodeId, SubNode.Builder<T> subNodeBuilderTemplate) {
        return registerSubNode(subNodeId, subNodeBuilderTemplate, null);
    }

    protected final <T extends GenericNode> SubNode<T> registerSubNode(String subNodeId, SubNode.Builder<T> subNodeBuilderTemplate, Identifier newSubNodeValueIdentifier) {
        SubNode<T> subNode = subNodeBuilderTemplate.build(this, subNodeId, newSubNodeValueIdentifier);
        subNodes.put(subNodeId, subNode);
        return subNode;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* GenericNode Interface */

    /**
     *  {
     *      node_type: "<@node_identifier>",
     *      gui_data: {...},
     *      sub_nodes: {
     *          <@subNodeId>: {...}, 
     *          ... , 
     *          <@subNodeId>: {...}
     *      }
     *  }
     */
    @Override
    public NbtCompound toNbt(ToNbtSettings settings) {
        // get base nbt compound
        NbtCompound nbt = super.toNbt(settings);

        // sub_nodes: {}
        NbtCompound subNodesCompound = new NbtCompound();
        // add all <@subNodeId>: {...} to sub_nodes
        for (Map.Entry<String, SubNode<? extends GenericNode>> entry : subNodes.entrySet()) {
            /* we might get empty compound (if we save execution data only), in this case we don't need to save it */
            NbtCompound entryNodeNbt = entry.getValue().getNode().toNbt(settings);
            if (!entryNodeNbt.isEmpty()) subNodesCompound.put(entry.getKey(), entryNodeNbt);
        }
        // add sub_nodes to nbt
        if (!subNodesCompound.isEmpty()) nbt.put("sub_nodes", subNodesCompound);
        
        return nbt;
    }

    @Override
    public GenericNode setFromNbt(NbtCompound nbt, FromNbtSettings settings) {
        // set guiData
        getGuiData().setFromNbt(nbt.getCompound("gui_data"));
        // set subNodes
        NbtCompound subNodesCompound = nbt.getCompound("sub_nodes");
        subNodesCompound.getKeys().forEach(key -> {
            NbtCompound subNodeNbt = subNodesCompound.getCompound(key);
            // modify subNode class if requested
            if (settings.buildNode()) {
                Identifier nodeIdentifier = Identifier.tryParse(subNodeNbt.getString("node_type"));
                modifySubNode(key, nodeIdentifier);
            }
            // recursive
            SubNode<?> subNode = getSubNode(key);
            if (subNode != null) {
                subNode.getNode().setFromNbt(subNodeNbt, settings);
            } 
        });
        return this;
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodes() {
        
        Map<String, NodeResult> returnSubNodes = new LinkedHashMap<>();
        for (Map.Entry<String, SubNode<? extends GenericNode>> subNode : subNodes.entrySet()) {
            // generate path
            List<String> subNodePath = new ArrayList<>();
            subNodePath.add(subNode.getKey());
            // this sub node
            returnSubNodes.put(GenericNode.listPathToStringPath(subNodePath), new NodeResult(new NodePath(subNodePath, subNode.getKey()), subNode.getValue().getNode()));
        }
        return returnSubNodes;
    }
    
    @Override
    public final Map<String, NodeResult> getAllSubNodesRecursive(List<String> pathStart) {
        
        Map<String, NodeResult> returnSubNodes = new LinkedHashMap<>();
        for (Map.Entry<String, SubNode<? extends GenericNode>> subNode : subNodes.entrySet()) {
            // generate path
            List<String> subNodePath = new ArrayList<>(pathStart);
            subNodePath.add(subNode.getKey());
            // this sub node
            returnSubNodes.put(GenericNode.listPathToStringPath(subNodePath), new NodeResult(new NodePath(subNodePath, subNode.getKey()), subNode.getValue().getNode()));
            // recursive
            returnSubNodes.putAll(subNode.getValue().getNode().getAllSubNodesRecursive(subNodePath));
        }
        return returnSubNodes;
    }
    
    @Override
    public final SubNode<? extends GenericNode> getSubNode(String path) {
        // if the subNodeId is already registered
        if (path != null && subNodes.containsKey(path)) {
            return subNodes.get(path);
        }
        return null;
    }
    
    @Override
    public final boolean modifySubNode(String path, Identifier newSubNodeValueIdentifier) {
        // if the subNodeId is already registered
        if (subNodes.containsKey(path)) {
            if (subNodes.get(path).setNodeClass(newSubNodeValueIdentifier)) {
                return true;
            }
        }
        System.out.println("failed to modify sub node " + path + " with " + newSubNodeValueIdentifier);
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
