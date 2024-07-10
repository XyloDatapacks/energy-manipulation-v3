package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.gui;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithList;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.ValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeResult;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.SubNodeData;
import net.minecraft.nbt.NbtCompound;

import java.util.List;
import java.util.Map;

public class GuiManager {
    private GenericNode rootNode;
    private String selectedNodePath = "";

    public GuiManager(GenericNode rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * set the root node to null and selectedNodePath empty
     * should only be called by the server
     */
    public void reset() {
        rootNode = null;
        selectedNodePath = "";
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* Set and Get */

    /**
     * set the new value for the root node
     * (can make selectedNodePath empty)
     * should only be called by the server
     */
    public void setRootNode(GenericNode rootNode)
    {
        this.rootNode = rootNode;
        if (rootNode != null) {
            // if selectedNodePath is no longer valid, reset
            if (!isNodeValid(selectedNodePath)) {
                selectedNodePath = "";
            }
        }
        // if root node is null, reset selected path
        else {
            selectedNodePath = "";
        }
    }
    
    public GenericNode getRootNode()
    {
        return rootNode;
    }
    
    public Map<String, NodeResult> getAllNodes() {
        if (rootNode != null) {
            return rootNode.getAllSubNodesRecursive();
        }
        return Map.of();
    }

    public Map<String, NodeResult> getRootSubNodes() {
        if (rootNode != null) {
            return rootNode.getAllSubNodes();
        }
        return Map.of();
    }

    public Map<String, NodeResult> getSubNodes(NodeResult nodeResult) {
        if (nodeResult != null) {
            return nodeResult.node().getAllSubNodes();
        }
        return Map.of();
    }
    
    public NodeResult getNodeAtPath(String path) {
        return rootNode.getNodeResultFromPath(path);
    }
    
    public String getPathAtIndex(int index) {
        Map<String, NodeResult> nodeResultMap = getAllNodes();
        List<String> keyList = nodeResultMap.keySet().stream().toList();
        return index < keyList.size() ? keyList.get(index) : null;
    }
    
    public boolean isNodeValid(String path) {
        return rootNode != null && rootNode.getNodeFromPath(path) != null;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* Selected Node */
    
    public String getSelectedNodePath() {
        return selectedNodePath;
    }
    
    /**
     * set the new selectedNodePath
     * should only be called by the server
     */
    public boolean selectNode(String path) {
        if (isNodeValid(path)) {
            selectedNodePath = path;
            return true;
        }
        selectedNodePath = "";
        return false;
    }

    /**
     * set the new selectedNodePath
     * should only be called by the server
     */
    public boolean selectNode(NodeResult nodeResult) {
        String path = GenericNode.listPathToStringPath(nodeResult.path().list());
        if (isNodeValid(path)) {
            selectedNodePath = path;
            return true;
        }
        selectedNodePath = "";
        return false;
    }
    
    public NodeResult getSelectedNode() {
        if (rootNode != null) {
            return rootNode.getNodeResultFromPath(selectedNodePath);
        }
        return null;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* Class Management */

    public void setNextNodeClass() {
        GuiManager.setNextNodeClass(getSelectedNode());
    }

    public void setPreviousNodeClass() {
        GuiManager.setPreviousNodeClass(getSelectedNode());
    }
    
    public static void setNextNodeClass(NodeResult nodeResult) {
        GenericNode parentNode = nodeResult.node().getParentNode();
        String path = NodeResult.getLastPathElement(nodeResult);
        parentNode.getSubNode(path).setNextNodeClass();
    }

    public static void setPreviousNodeClass(NodeResult nodeResult) {
        GenericNode parentNode = nodeResult.node().getParentNode();
        String path = NodeResult.getLastPathElement(nodeResult);
        parentNode.getSubNode(path).setPreviousNodeClass();
    }

    /*----------------------------------------------------------------------------------------------------------------*/


    /*----------------------------------------------------------------------------------------------------------------*/
    /* Value Management */

    public void setSelectedNodeValue(Object value) {
        if (getSelectedNode().node() instanceof AbstractNodeWithValue<?> nodeWithValue) {
            nodeWithValue.setValueFromObject(value);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* List Management */

    public void removeNodeFromList(String path) {
        GenericNode parentNode = getNodeAtPath(path).node().getParentNode();
        if (parentNode instanceof AbstractNodeWithList<?> listNode) {
            listNode.removeSubNode(path);
        }
    }

    public void addEntryToList(String path) {
        GenericNode node = getNodeAtPath(path).node();
        if (node instanceof AbstractNodeWithList<?> listNode) {
            listNode.appendSubNode();
        }
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* nbt */
    
    public NbtCompound toNbt() {
        if (rootNode != null) {
            return rootNode.toNbt();
        }
        return new NbtCompound();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* Display */
    
    public static ButtonDisplay getButtonDisplay(NodeResult nodeResult) {
        GenericNode node = nodeResult.node();
        GenericNode parentNode = node.getParentNode();
        String id = nodeResult.path().id();
        
        SubNodeData subNodeData = parentNode.getSubNodeData(id);
        NodeData<? extends GenericNode> nodeData = node.getNodeData();
        
        if (nodeData != null) {
            String nodeName = node instanceof ValueTypeNode<?> nodeValue ? nodeValue.getValueDisplay() : nodeData.name();
            
            if (subNodeData != null) {
                return new ButtonDisplay(subNodeData.name(), nodeName);
            }
            return new ButtonDisplay("null", nodeName);
        }
        return new ButtonDisplay("error", "error");
    }
    
    public static EditorInfo getEditorHeader(NodeResult nodeResult) {
        GenericNode parentNode = nodeResult.node().getParentNode();
        String id = nodeResult.path().id();
        
        SubNodeData subNodeData = parentNode.getSubNodeData(id);

        if (subNodeData != null) {
            return new EditorInfo(subNodeData.name(), subNodeData.description());
        }
        return new EditorInfo("error", "error");
    }

    public static EditorInfo getEditorCurrentSelection(NodeResult nodeResult) {
        GenericNode node = nodeResult.node();

        NodeData<? extends GenericNode> nodeData = node.getNodeData();

        if (nodeData != null) {
            return new EditorInfo(nodeData.name(), nodeData.description());
        }
        return new EditorInfo("error", "error");
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    
    





    public record ButtonDisplay(String subNodeName, String nodeName) {}
    public record EditorInfo(String name, String description) {}
}
