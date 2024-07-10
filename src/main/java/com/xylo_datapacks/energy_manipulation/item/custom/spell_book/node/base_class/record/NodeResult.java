package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.GenericNode;

import java.util.ArrayList;
import java.util.List;

public record NodeResult(NodePath path, GenericNode node) {
    
    public static String getParentPath(NodeResult nodeResult) {
        List<String> parentNodeListPath = new ArrayList<>(nodeResult.path().list());
        parentNodeListPath.remove(parentNodeListPath.size() - 1);
        return GenericNode.listPathToStringPath(parentNodeListPath);
    }
    
    public static String getLastPathElement(NodeResult nodeResult) {
        return nodeResult.path().list().get(nodeResult.path().list().size() - 1);
    }
}
