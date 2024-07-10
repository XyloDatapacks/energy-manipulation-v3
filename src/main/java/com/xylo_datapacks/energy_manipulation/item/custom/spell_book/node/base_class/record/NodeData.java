package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record NodeData<T extends GenericNode>(Identifier identifier, String name, String description, Supplier<T> nodeSupplier, Map<String, SubNodeData> subNodes) {
    
    public static final class Builder<T extends GenericNode> {
        private final Identifier identifier;
        private final String name;
        private final String description;
        private final Supplier<T> nodeSupplier;
        private Map<String, SubNodeData> subNodes = new HashMap<>();

        public Builder(Identifier identifier, NodeDataMaker<T> nodeDataMaker) {
            this.identifier = identifier;
            this.name = nodeDataMaker.name;
            this.description = nodeDataMaker.description;
            this.nodeSupplier = nodeDataMaker.nodeSupplier;
            this.subNodes = nodeDataMaker.subNodes;
        }

        public NodeData<T> build() {
            return new NodeData<T>(identifier, name, description, nodeSupplier, subNodes);
        }
    }
    
    public record NodeDataMaker<T extends GenericNode>(String name, String description, Supplier<T> nodeSupplier, Map<String, SubNodeData> subNodes) {}
}
