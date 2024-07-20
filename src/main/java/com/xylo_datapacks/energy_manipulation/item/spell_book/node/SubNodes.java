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

public class SubNodes {
    public static List<NodeData<? extends IntegerNumberNode>> TEMPLATE_ALL_INTEGER = List.of(
            Nodes.VALUE_TYPE_INTEGER
    );
    public static List<NodeData<? extends DoubleNumberNode>> TEMPLATE_ALL_DOUBLE = List.of(
            Nodes.VALUE_TYPE_DOUBLE
    );
    public static List<NodeData<? extends InstructionNode>> TEMPLATE_COMMON_INSTRUCTION = List.of(
            Nodes.INSTRUCTION_GENERATE_SHAPE,
            Nodes.INSTRUCTION_MODIFY_POSITION,
            Nodes.INSTRUCTION_IF,
            Nodes.INSTRUCTION_DELAY,
            Nodes.INSTRUCTION_WHILE_LOOP,
            Nodes.INSTRUCTION_OUTPUT,
            Nodes.INSTRUCTION_CONTINUE,
            Nodes.INSTRUCTION_BREAK
    );



    public static SubNodeDefinition<InstructionNode> COMMON_INSTRUCTION = new SubNodeDefinition<>("instruction", new SubNode.Builder<InstructionNode>()
            .addNodeValues(TEMPLATE_COMMON_INSTRUCTION),
            Nodes.INSTRUCTION_GENERATE_SHAPE.identifier());

    public static SubNodeDefinition<InstructionNode> CONDITIONAL_INSTRUCTION = new SubNodeDefinition<>("instruction", new SubNode.Builder<InstructionNode>()
            .addNodeValues(TEMPLATE_COMMON_INSTRUCTION)
            .addNodeValues(List.of()),
            Nodes.INSTRUCTION_GENERATE_SHAPE.identifier());

    public static SubNodeDefinition<InstructionNode> LOOP_INSTRUCTION = new SubNodeDefinition<>("instruction", new SubNode.Builder<InstructionNode>()
            .addNodeValues(TEMPLATE_COMMON_INSTRUCTION)
            .addNodeValues(List.of()),
            Nodes.INSTRUCTION_GENERATE_SHAPE.identifier());

    
    
    public static SubNodeDefinition<InstructionProviderNode> COMMON_INSTRUCTION_PROVIDER = new SubNodeDefinition<>("instruction_provider", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValue(Nodes.INSTRUCTION_PROVIDER, null),
            Nodes.INSTRUCTION_PROVIDER.identifier());

    public static SubNodeDefinition<InstructionProviderNode> CONDITIONAL_INSTRUCTION_PROVIDER = new SubNodeDefinition<>("instruction_provider", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValue(Nodes.INSTRUCTION_PROVIDER, () -> new InstructionProviderNode(CONDITIONAL_INSTRUCTION)),
            Nodes.INSTRUCTION_PROVIDER.identifier());

    public static SubNodeDefinition<InstructionProviderNode> LOOP_INSTRUCTION_PROVIDER = new SubNodeDefinition<>("instruction_provider", new SubNode.Builder<InstructionProviderNode>()
            .addNodeValue(Nodes.INSTRUCTION_PROVIDER, () -> new InstructionProviderNode(LOOP_INSTRUCTION)),
            Nodes.INSTRUCTION_PROVIDER.identifier());





    public static SubNodeDefinition<BooleanNode> CONDITION = new SubNodeDefinition<>("condition", new SubNode.Builder<BooleanNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_BOOLEAN, 
                    Nodes.CONDITION_CASTER_ON_GROUND
            )),
            Nodes.VALUE_TYPE_BOOLEAN.identifier());
    
    public static SubNodeDefinition<NumberNode> NUMBER = new SubNodeDefinition<>("number", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_INTEGER,
                    Nodes.NUMBER_INTEGER,
                    Nodes.VALUE_TYPE_DOUBLE,
                    Nodes.NUMBER_DOUBLE
            )),
            Nodes.VALUE_TYPE_INTEGER.identifier());

    public static SubNodeDefinition<IntegerNumberNode> INTEGER = new SubNodeDefinition<>("integer", new SubNode.Builder<IntegerNumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_INTEGER,
                    Nodes.NUMBER_INTEGER
            )),
            Nodes.VALUE_TYPE_INTEGER.identifier());

    public static SubNodeDefinition<DoubleNumberNode> DOUBLE = new SubNodeDefinition<>("double", new SubNode.Builder<DoubleNumberNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_DOUBLE,
                    Nodes.NUMBER_DOUBLE
            )),
            Nodes.VALUE_TYPE_DOUBLE.identifier());
    
    
    
    public record SubNodeDefinition<T extends GenericNode>(String subNodeId, SubNode.Builder<T> subNodeBuilderTemplate, Identifier newSubNodeValueIdentifier) {}
}
