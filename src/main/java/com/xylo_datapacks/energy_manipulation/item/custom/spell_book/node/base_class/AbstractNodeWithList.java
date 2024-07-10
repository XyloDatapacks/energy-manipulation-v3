package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodePath;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeResult;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

import java.util.*;

public abstract class AbstractNodeWithList<T extends GenericNode> extends AbstractNode {
    private final String subNodesId;
    private final SubNode.Builder<T> subNodeBuilderTemplate;
    private final List<SubNode<T>> subNodes = new ArrayList<>();
    
    public AbstractNodeWithList(NodeData nodeData, String subNodesId, SubNode.Builder<T> subNodeBuilderTemplate) {
        super(nodeData);
        this.subNodesId = subNodesId;
        this.subNodeBuilderTemplate = subNodeBuilderTemplate;
    }

    public final void appendSubNode() {
        subNodes.add(subNodes.size(), subNodeBuilderTemplate.build(this));
    }
    
    public final void appendSubNode(Identifier newSubNodeValueIdentifier) {
        subNodes.add(subNodes.size(), subNodeBuilderTemplate.build(this, newSubNodeValueIdentifier));
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
    
    public final void prependSubNode(Identifier newSubNodeValueIdentifier) {
        subNodes.add(0, subNodeBuilderTemplate.build(this, newSubNodeValueIdentifier));
    }
    
    public final void insertSubNode(int index, Identifier newSubNodeValueIdentifier) {
        subNodes.add(index, subNodeBuilderTemplate.build(this, newSubNodeValueIdentifier));
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
    
    public final SubNode<T> removeFirstSubNode() {
        return subNodes.remove(0);
    }
    
    public final SubNode<T> removeLastSubNode() {
        return subNodes.remove(subNodes.size() - 1);
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
    public final NbtCompound toNbt() {
        // get base nbt compound
        NbtCompound nbt = super.toNbt();
        
        // sub_nodes: []
        NbtList subNodesList = new NbtList();
        // add all {...} to sub_nodes
        for (SubNode<? extends GenericNode> entry : subNodes) {
            GenericNode node = entry.getNode();
            subNodesList.add(node.toNbt());
        }
        // add sub_nodes to nbt
        nbt.put("sub_nodes", subNodesList);
        
        return nbt;
    }

    @Override
    public GenericNode setFromNbt(NbtCompound nbt) {
        // set guiData
        getGuiData().setFromNbt(nbt.getCompound("gui_data"));
        // set subNodes
        nbt.getList("sub_nodes", NbtCompound.COMPOUND_TYPE).forEach(compound -> {
            NbtCompound subNodeNbt = ((NbtCompound) compound);
            Identifier nodeIdentifier = Identifier.tryParse(subNodeNbt.getString("node_type"));
            appendSubNode(nodeIdentifier);
            SubNode<?> subNode = getSubNode(subNodes.size() - 1);
            if (subNode != null) {
                subNode.getNode().setFromNbt(subNodeNbt);
            }
        });
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
