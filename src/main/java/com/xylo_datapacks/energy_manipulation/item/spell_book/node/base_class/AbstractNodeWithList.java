package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractNodeWithList<T extends GenericNode> extends AbstractNode {
    private final String subNodesId;
    private final List<SubNode<T>> subNodes = new ArrayList<>();
    private final Function<Identifier, SubNode<T>> buildSubNode;
    
    public AbstractNodeWithList(NodeData<?> nodeData, String subNodesId, SubNode.Builder<T> subNodeBuilderTemplate, Identifier defaultSubNodeValueIdentifier) {
        super(nodeData);
        this.subNodesId = subNodesId;
        this.buildSubNode = (newSubNodeValueIdentifier) -> subNodeBuilderTemplate.build(this, subNodesId, Optional.ofNullable(newSubNodeValueIdentifier).orElse(defaultSubNodeValueIdentifier));
    }

    public AbstractNodeWithList(NodeData<?> nodeData, SubNodes.SubNodeDefinition<T> definition) {
        this(nodeData, definition.subNodeId(), definition.subNodeBuilderTemplate(), definition.newSubNodeValueIdentifier());  
    }

    public AbstractNodeWithList(NodeData<?> nodeData, String subNodesId, SubNodes.SubNodeDefinition<T> definition) {
        this(nodeData, subNodesId, definition.subNodeBuilderTemplate(), definition.newSubNodeValueIdentifier());    
    }
    
    public int getSubNodesSize() {
        return subNodes.size();
    }

    public final SubNode<T> getSubNode(int index) {
        if (index >= 0 && index < subNodes.size()) {
            return subNodes.get(index);
        }
        return null;
    }
    
    public List<SubNode<T>> getSubNodes() {
        return Collections.unmodifiableList(subNodes);
    }

    public final boolean modifySubNode(int index, Identifier newSubNodeValueIdentifier) {
        if (index >= 0 && index < subNodes.size()) {
            return subNodes.get(index).setNodeClass(newSubNodeValueIdentifier);
        }
        return false;
    }

    public final void appendSubNode() {
        subNodes.add(subNodes.size(), buildSubNode.apply(null));
    }

    public final void appendSubNode(Identifier newSubNodeValueIdentifier) {
        subNodes.add(subNodes.size(), buildSubNode.apply(newSubNodeValueIdentifier));
    }
    
    public final void prependSubNode(Identifier newSubNodeValueIdentifier) {
        subNodes.add(0, buildSubNode.apply(newSubNodeValueIdentifier));
    }
    
    public final void insertSubNode(int index, Identifier newSubNodeValueIdentifier) {
        subNodes.add(index, buildSubNode.apply(newSubNodeValueIdentifier));
    }

    public final SubNode<T> removeSubNode(int index) {
        return subNodes.remove(index);
    }

    public final SubNode<T> removeSubNode(String path) {
        return removeSubNode(GenericNode.stringPathToListPath(path));
    }

    public final SubNode<T> removeSubNode(List<String> listPath) {
        return removeSubNode(GenericNode.stripIndexFromPathElement(listPath.get(listPath.size() - 1)));
    }
    
    public final void removeAllSubNodes() {
        subNodes.clear();
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* GenericNode Interface */

    /**
     *  {
     *      node_type: "<@node_identifier>",
     *      gui_data: {...},
     *      sub_nodes: [
     *          {...}, 
     *          ... , 
     *          {...}
     *      ]
     *  }
     */
    @Override
    public NbtCompound toNbt(ToNbtSettings settings) {
        // get base nbt compound
        NbtCompound nbt = super.toNbt(settings);
        
        // sub_nodes: []
        NbtList subNodesList = new NbtList();
        // add all {...} to sub_nodes
        boolean areAllEmpty = true; // check if [{},{},{},...,{}] with no data for any subNode
        for (SubNode<? extends GenericNode> entry : subNodes) {
            /* even tho we might get an empty compound (if we save execution data only), add the sub node because we might have:
             * [{},{},{node_type:"...",...},{}]. After adding all to the list, if all compounds are empty, don't add sub_nodes compound*/
            NbtCompound entryNodeNbt = entry.getNode().toNbt(settings);
            subNodesList.add(entryNodeNbt);
            if (!entryNodeNbt.isEmpty()) areAllEmpty = false;
        }
        // add sub_nodes to nbt
        if (!subNodesList.isEmpty() && !areAllEmpty) nbt.put("sub_nodes", subNodesList);
        
        return nbt;
    }

    @Override
    public GenericNode setFromNbt(NbtCompound nbt, FromNbtSettings settings) {
        // set guiData
        getGuiData().setFromNbt(nbt.getCompound("gui_data"));
        // set subNodes
        NbtList nbtList = nbt.getList("sub_nodes", NbtCompound.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            NbtCompound subNodeNbt = nbtList.getCompound(i);
            // add subNode if requested
            if (settings.buildNode()) {
                Identifier nodeIdentifier = Identifier.tryParse(subNodeNbt.getString("node_type"));
                appendSubNode(nodeIdentifier);
            }
            // recursive
            SubNode<?> subNode = getSubNode(i);
            if (subNode != null) {
                subNode.getNode().setFromNbt(subNodeNbt, settings);
            }
        }
        return this;
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodes() {
        
        Map<String, NodeResult> returnSubNodes = new LinkedHashMap<>();
        for (int index = 0; index < subNodes.size(); index++) {
            // generate path
            List<String> subNodePath = new ArrayList<>();
            subNodePath.add(subNodesId + "[" + index + "]");
            // this sub node
            returnSubNodes.put(GenericNode.listPathToStringPath(subNodePath), new NodeResult(new NodePath(subNodePath, subNodesId), subNodes.get(index).getNode()));
        }
        return returnSubNodes;
    }

    @Override
    public final Map<String, NodeResult> getAllSubNodesRecursive(List<String> pathStart) {
       
        Map<String, NodeResult> returnSubNodes = new LinkedHashMap<>();
        for (int index = 0; index < subNodes.size(); index++) {
            // generate path
            List<String> subNodePath = new ArrayList<>(pathStart);
            subNodePath.add(subNodesId + "[" + index + "]");
            // this sub node
            returnSubNodes.put(GenericNode.listPathToStringPath(subNodePath), new NodeResult(new NodePath(subNodePath, subNodesId), subNodes.get(index).getNode()));
            // recursive
            returnSubNodes.putAll(subNodes.get(index).getNode().getAllSubNodesRecursive(subNodePath));
        }
        return returnSubNodes;
    }
    
    @Override
    public final SubNode<? extends GenericNode> getSubNode(String path) {
        return getSubNode(GenericNode.stripIndexFromPathElement(path));
    }

    @Override
    public final boolean modifySubNode(String path, Identifier newSubNodeValueIdentifier) {
        if (modifySubNode(GenericNode.stripIndexFromPathElement(path), newSubNodeValueIdentifier)) {
            return true;
        }
        System.out.println("failed to modify sub node " + path + " with " + newSubNodeValueIdentifier);
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
