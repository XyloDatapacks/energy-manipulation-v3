package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeData;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class SubNode<T extends GenericNode> {
    private T node;
    private final Map<Identifier, Supplier<T>> nodeClasses;
    private Identifier SelectedClassIdentifier;
    
    private SubNode(GenericNode parentNode, Map<Identifier, Supplier<T>> nodeValues, Identifier SelectedValue) {
        this.nodeClasses = nodeValues;
        this.SelectedClassIdentifier = SelectedValue;
        this.node = nodeValues.get(SelectedClassIdentifier).get();
        ((AbstractNode) this.node).setParentNode(parentNode);
    }
    
    /**
     * @return node contained by this object
     */
    public T getNode() {
        return node;
    }

    /**
     * @param identifier the identifier of the node class to use, will be checked against nodeClasses
     * @return true if the operation was successful
     */
    public boolean setNodeClass(Identifier identifier) {
        if (this.nodeClasses.containsKey(identifier)) {
            this.SelectedClassIdentifier = identifier;
            // update class
            GenericNode parentNode = this.node.getParentNode();
            this.node = this.nodeClasses.get(SelectedClassIdentifier).get();
            ((AbstractNode) this.node).setParentNode(parentNode);
            return true;
        }
        return false;
    }

    /** 
     * Cycle through the classes toward the left
     */
    public void setNextNodeClass() {
        List<Identifier> ClassIdentifierList = nodeClasses.keySet().stream().toList();
        int newIndex = ClassIdentifierList.indexOf(SelectedClassIdentifier) + 1;
        newIndex = newIndex < ClassIdentifierList.size() ? newIndex : 0; 
        Identifier newIdentifier = ClassIdentifierList.get(newIndex);
        setNodeClass(newIdentifier);
    }

    /**
     * Cycle through the classes toward the right
     */
    public void setPreviousNodeClass() {
        List<Identifier> ClassIdentifierList = nodeClasses.keySet().stream().toList();
        int newIndex = ClassIdentifierList.indexOf(SelectedClassIdentifier) - 1;
        newIndex = newIndex >= 0 ? newIndex : ClassIdentifierList.size() - 1;
        Identifier newIdentifier = ClassIdentifierList.get(newIndex);
        setNodeClass(newIdentifier);
    }

    /**
     * @return map with node identifier as key, and node supplier as value
     */
    public Map<Identifier, Supplier<T>> getPossibleNodeClasses() {
        return nodeClasses;
    }

    /**
     * @return pair with selected node identifier as left, and selected node supplier as right
     */
    public Pair<Identifier, Supplier<T>> getSelectedClass() {
        if (nodeClasses.containsKey(SelectedClassIdentifier)) {
            return new Pair<>(SelectedClassIdentifier, nodeClasses.get(SelectedClassIdentifier));
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static final class Builder<T extends GenericNode> {
        private final Map<Identifier, Supplier<T>> nodeClasses = new HashMap<>();
        private Identifier selectedClassIdentifier;
        
        public Map<Identifier, Supplier<T>> getPossibleNodeValues() {
            return nodeClasses;
        }
        
        public Builder() {
        }

        /**
         * nodeData type (S) is defined by the class of the supplier it contains
         * @param nodeData node identifier of the node we want to add as possible value
         */
        public Builder<T> addNodeValues(List<NodeData<? extends T>> nodeData) {
            nodeData.forEach( singleNodeData -> { addNodeValue(singleNodeData, null); });
            return this;
        }

        /**
         * nodeData type (S) is defined by the class of the supplier it contains
         * @param nodeData node we want to add as possible value
         * @param customSupplier node supplier to use instead of the default one
         */
        public <S extends T> Builder<T> addNodeValue(NodeData<S> nodeData, Supplier<S> customSupplier) {
            this.nodeClasses.put(nodeData.identifier(), (Supplier<T>) (customSupplier != null ? customSupplier : nodeData.nodeSupplier()) );
            return this;
        }

        /**
         * call to finalize the construction of the sub node
         * @param defaultValue identifier of one of the node values added. if not found, use first one instead
         */
        public SubNode<T> build(GenericNode parentNode, Identifier defaultValue) {
            if (nodeClasses.containsKey(defaultValue)) {
                selectedClassIdentifier = defaultValue;
                return new SubNode<>(parentNode, nodeClasses, selectedClassIdentifier);
            } 
           return build(parentNode);
        }

        /**
         * call to finalize the construction of the sub node
         */
        public SubNode<T> build(GenericNode parentNode) {
            selectedClassIdentifier = nodeClasses.keySet().iterator().next();
            return new SubNode<>(parentNode, nodeClasses, selectedClassIdentifier);
        }
    }
} 