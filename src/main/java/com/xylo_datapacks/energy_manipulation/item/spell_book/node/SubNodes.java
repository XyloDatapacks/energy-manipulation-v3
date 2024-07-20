package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.InstructionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.InstructionProviderNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SubNodes {
    public static List<NodeData<? extends IntegerNumberNode>> TEMPLATE_ALL_INTEGER = List.of(
            Nodes.VALUE_TYPE_INTEGER
    );
    public static List<NodeData<? extends DoubleNumberNode>> TEMPLATE_ALL_DOUBLE = List.of(
            Nodes.VALUE_TYPE_DOUBLE
    );
    
    
    public static SubNodeDefinition<BooleanNode> CONDITION = new SubNodeDefinition<>("condition", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN, 
                    Nodes.CONDITION_CASTER_ON_GROUND)),
            Nodes.VALUE_TYPE_BOOLEAN.identifier());

    public static SubNodeDefinition<InstructionProviderNode> INSTRUCTIONS = new SubNodeDefinition<>("instructions", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValues(List.of(
                    Nodes.INSTRUCTION_PROVIDER)),
            Nodes.INSTRUCTION_PROVIDER.identifier());

    public static SubNodeDefinition<NumberNode> NUMBER = new SubNodeDefinition<>("number", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_INTEGER,
                    Nodes.NUMBER_INTEGER,
                    Nodes.VALUE_TYPE_DOUBLE,
                    Nodes.NUMBER_DOUBLE)),
            Nodes.VALUE_TYPE_INTEGER.identifier());

    public static SubNodeDefinition<IntegerNumberNode> INTEGER = new SubNodeDefinition<>("integer", new SubNode.Builder<IntegerNumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_INTEGER,
                    Nodes.NUMBER_INTEGER)),
            Nodes.VALUE_TYPE_INTEGER.identifier());

    public static SubNodeDefinition<DoubleNumberNode> DOUBLE = new SubNodeDefinition<>("double", new SubNode.Builder<DoubleNumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_DOUBLE,
                    Nodes.NUMBER_DOUBLE)),
            Nodes.VALUE_TYPE_DOUBLE.identifier());
    
    
    
    public record SubNodeDefinition<T extends GenericNode>(String subNodeId, SubNode.Builder<T> subNodeBuilderTemplate, Identifier newSubNodeValueIdentifier) {}
}
