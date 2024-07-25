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
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.DoubleOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.IntegerOperatorNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import net.minecraft.util.Identifier;

import java.util.List;

public class SubNodes {
    /** Used by ComplexIntegerNumberNode */
    public static List<NodeData<? extends IntegerNumberNode>> TEMPLATE_ALL_INTEGER = List.of(
            Nodes.VALUE_TYPE_INTEGER,
            Nodes.VARIABLE_GETTER_INTEGER,
            Nodes.OPERATION_INTEGER_NUMBER
    );
    /** Used by ComplexDoubleNumberNode */
    public static List<NodeData<? extends DoubleNumberNode>> TEMPLATE_ALL_DOUBLE = List.of(
            Nodes.VALUE_TYPE_DOUBLE,
            Nodes.VARIABLE_GETTER_DOUBLE,
            Nodes.OPERATION_DOUBLE_NUMBER
    );
    /** Used by ComplexStringNode */
    public static List<NodeData<? extends StringNode>> TEMPLATE_ALL_STRINGS = List.of(
            Nodes.VALUE_TYPE_STRING,
            Nodes.NUMBER_INTEGER,
            Nodes.NUMBER_DOUBLE,
            Nodes.VARIABLE_GETTER_STRING
    );
    public static List<NodeData<? extends InstructionNode>> TEMPLATE_COMMON_INSTRUCTION = List.of(
            Nodes.INSTRUCTION_GENERATE_SHAPE,
            Nodes.INSTRUCTION_MODIFY_POSITION,
            Nodes.INSTRUCTION_IF,
            Nodes.INSTRUCTION_DELAY,
            Nodes.INSTRUCTION_WHILE_LOOP,
            Nodes.INSTRUCTION_OUTPUT,
            Nodes.INSTRUCTION_CONTINUE,
            Nodes.INSTRUCTION_BREAK,
            Nodes.INSTRUCTION_CREATE_VARIABLE,
            Nodes.INSTRUCTION_MODIFY_VARIABLE
    );
    public static List<NodeData<? extends IntegerOperatorNode>> TEMPLATE_INTEGER_NUMBER_OPERATORS = List.of(
            Nodes.OPERATOR_ADD,
            Nodes.OPERATOR_SUBTRACT,
            Nodes.OPERATOR_MULTIPLY,
            Nodes.OPERATOR_DIVIDE,
            Nodes.OPERATOR_EQUAL,
            Nodes.OPERATOR_GREATER_THEN,
            Nodes.OPERATOR_LESS_THEN
    );
    public static List<NodeData<? extends DoubleOperatorNode>> TEMPLATE_DOUBLE_NUMBER_OPERATORS = List.of(
            Nodes.OPERATOR_ADD,
            Nodes.OPERATOR_SUBTRACT,
            Nodes.OPERATOR_MULTIPLY,
            Nodes.OPERATOR_DIVIDE,
            Nodes.OPERATOR_EQUAL,
            Nodes.OPERATOR_GREATER_THEN,
            Nodes.OPERATOR_LESS_THEN
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
            .addNodeValue(Nodes.INSTRUCTION_PROVIDER),
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

    public static SubNodeDefinition<StringNode> STRING = new SubNodeDefinition<>("string", new SubNode.Builder<StringNode>()
            .addNodeValues(List.of(
                    Nodes.VALUE_TYPE_STRING,
                    Nodes.STRING
            )),
            Nodes.VALUE_TYPE_STRING.identifier());



    public static SubNodeDefinition<IntegerOperatorNode> INTEGER_NUMBER_OPERATOR = new SubNodeDefinition<>("integer_operator", new SubNode.Builder<IntegerOperatorNode>()
            .addNodeValues(TEMPLATE_INTEGER_NUMBER_OPERATORS),
            Nodes.OPERATOR_ADD.identifier());

    public static SubNodeDefinition<DoubleOperatorNode> DOUBLE_NUMBER_OPERATOR = new SubNodeDefinition<>("double_operator", new SubNode.Builder<DoubleOperatorNode>()
            .addNodeValues(TEMPLATE_DOUBLE_NUMBER_OPERATORS),
            Nodes.OPERATOR_ADD.identifier());





    public record SubNodeDefinition<T extends GenericNode>(String subNodeId, SubNode.Builder<T> subNodeBuilderTemplate, Identifier newSubNodeValueIdentifier) {}
}
