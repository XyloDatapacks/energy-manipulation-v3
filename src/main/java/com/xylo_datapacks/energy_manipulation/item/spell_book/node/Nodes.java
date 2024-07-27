package com.xylo_datapacks.energy_manipulation.item.spell_book.node;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.condition.CasterOnGroundConditionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect.BreakEffectNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect.EffectProviderNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.effect.FireEffectNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.ComplexDoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.ComplexIntegerNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.IntegerValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset.CardinalOffsetNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.offset.DirectionalOffsetNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.DoubleOperationNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.IntegerOperationNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.operation.operator.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.position.AlignPositionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.position.OffsetPositionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.SubNodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ProjectileShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.RayShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.ComplexStringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.VariableTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.DoubleVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.IntegerVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.getter.StringVariableGetterNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier.DoubleVariableModifierNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier.IntegerVariableModifierNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable.modifier.StringVariableModifierNode;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class Nodes {
    public static Map<Identifier, NodeData<? extends GenericNode>> NODES = new HashMap<>();

    public static final NodeData<SpellNode> SPELL = registerNode("spell", "spell", new NodeData.NodeDataMaker<>("Spell", "A Spell", SpellNode::new, Map.of(
            "program", new SubNodeData("program","Something that the spell will execute")
    )));
    
    /** Instructions */
    public static final NodeData<InstructionProviderNode> INSTRUCTION_PROVIDER = registerNode("instruction", "instruction_provider", new NodeData.NodeDataMaker<>("Instruction Provider", "List of instructions", InstructionProviderNode::new, Map.of(
            "instruction", new SubNodeData("Instruction","A single instruction")
    )));
    public static final NodeData<GenerateShapeInstructionNode> INSTRUCTION_GENERATE_SHAPE = registerNode("instruction", "generate_shape", new NodeData.NodeDataMaker<>("Generate Shape", "Generates a shape", GenerateShapeInstructionNode::new, Map.of(
            "shape", new SubNodeData("Shape","The shape to generate")
    )));
    public static final NodeData<ModifyPositionInstructionNode> INSTRUCTION_MODIFY_POSITION = registerNode("instruction", "modify_position", new NodeData.NodeDataMaker<>("Modify Position", "Changes the position context", ModifyPositionInstructionNode::new, Map.of(
            "position", new SubNodeData("Position","Position Context")
    )));
    public static final NodeData<DelayInstructionNode> INSTRUCTION_DELAY = registerNode("instruction", "delay", new NodeData.NodeDataMaker<>("Delay", "Stops execution for a certain time", DelayInstructionNode::new, Map.of(
            "time", new SubNodeData("Time","Length of the delay")
    )));
    public static final NodeData<BreakInstructionNode> INSTRUCTION_BREAK = registerNode("instruction", "break", new NodeData.NodeDataMaker<>("Break", "Stops execution of this loop", BreakInstructionNode::new, Map.of()));
    public static final NodeData<ContinueInstructionNode> INSTRUCTION_CONTINUE = registerNode("instruction", "continue", new NodeData.NodeDataMaker<>("Continue", "Skip to next iteration of this loop", ContinueInstructionNode::new, Map.of()));
    public static final NodeData<IfInstructionNode> INSTRUCTION_IF = registerNode("instruction", "if", new NodeData.NodeDataMaker<>("If", "execute instructions based on a condition", IfInstructionNode::new, Map.of(
            "condition", new SubNodeData("Condition","Condition to pass"),
            "passed", new SubNodeData("Passed","What happens if condition = true"),
            "failed", new SubNodeData("Failed","What happens if condition = false")
    )));
    public static final NodeData<WhileLoopInstructionNode> INSTRUCTION_WHILE_LOOP = registerNode("instruction", "while", new NodeData.NodeDataMaker<>("While", "execute instructions in loop as long as the condition is true", WhileLoopInstructionNode::new, Map.of(
            "condition", new SubNodeData("Condition","Condition to pass"),
            "body", new SubNodeData("Body","the instructions to run")
    )));
    public static final NodeData<OutputInstructionNode> INSTRUCTION_OUTPUT = registerNode("instruction", "output", new NodeData.NodeDataMaker<>("Output", "print a debug string", OutputInstructionNode::new, Map.of(
            "text", new SubNodeData("Text","the output message")
    )));
    public static final NodeData<CreateVariableInstructionNode> INSTRUCTION_CREATE_VARIABLE = registerNode("instruction", "create_variable", new NodeData.NodeDataMaker<>("Create Variable", "creates a new variable", CreateVariableInstructionNode::new, Map.of(
            "name", new SubNodeData("Name","the name of the variable"),
            "type", new SubNodeData("Type","the type of the variable")
    )));
    public static final NodeData<ModifyVariableInstructionNode> INSTRUCTION_MODIFY_VARIABLE = registerNode("instruction", "modify_variable", new NodeData.NodeDataMaker<>("Modify Variable", "modify the value of a variable", ModifyVariableInstructionNode::new, Map.of(
            "modifier", new SubNodeData("Type","the type of variable to modify")
    )));

    /** Shapes */
    public static final NodeData<ProjectileShapeNode> SHAPE_PROJECTILE = registerNode("shape", "projectile", new NodeData.NodeDataMaker<>("Projectile", "Projectile like spell", ProjectileShapeNode::new, Map.of(
            "effects", new SubNodeData("Effects","The effects to apply")
    )));
    public static final NodeData<RayShapeNode> SHAPE_RAY = registerNode("shape", "ray", new NodeData.NodeDataMaker<>("Ray", "Ray like spell", RayShapeNode::new, Map.of(
            "effects", new SubNodeData("Effects","The effects to apply")
    )));

    /** Effects */
    public static final NodeData<EffectProviderNode> EFFECT_PROVIDER = registerNode("effect", "effect_provider", new NodeData.NodeDataMaker<>("Effect Provider", "List of effects", EffectProviderNode::new, Map.of(
            "effect", new SubNodeData("Effect","A single effect")
    )));
    public static final NodeData<BreakEffectNode> EFFECT_BREAK = registerNode("effect", "break", new NodeData.NodeDataMaker<>("Break", "Breaks a block", BreakEffectNode::new, Map.of(
            
    )));
    public static final NodeData<FireEffectNode> EFFECT_FIRE = registerNode("effect", "fire", new NodeData.NodeDataMaker<>("Fire", "Set on fire", FireEffectNode::new, Map.of(
            
    )));

    /** Position */
    public static final NodeData<OffsetPositionNode> POSITION_OFFSET = registerNode("position", "offset", new NodeData.NodeDataMaker<>("Offset", "Offsets the context position", OffsetPositionNode::new, Map.of(
            "offset", new SubNodeData("Offset","the offset to apply")
    )));
    public static final NodeData<AlignPositionNode> POSITION_ALIGN = registerNode("position", "align", new NodeData.NodeDataMaker<>("Align", "Align the position to one or more axis", AlignPositionNode::new, Map.of(
            "x", new SubNodeData("X","Align to the X axis"),
            "y", new SubNodeData("Y","Align to the Y axis"),
            "z", new SubNodeData("Z","Align to the Z axis")
    )));

    /** Offset */
    public static final NodeData<CardinalOffsetNode> OFFSET_CARDINAL = registerNode("offset", "cardinal", new NodeData.NodeDataMaker<>("Cardinal", "Offsets the context position in world space coords", CardinalOffsetNode::new, Map.of(
            "x", new SubNodeData("X","X axis offset"),
            "y", new SubNodeData("Y","Y axis offset"),
            "z", new SubNodeData("Z","Z axis offset")
    )));
    public static final NodeData<DirectionalOffsetNode> OFFSET_DIRECTIONAL = registerNode("offset", "directional", new NodeData.NodeDataMaker<>("Directional", "Offsets the context position in view space coords", DirectionalOffsetNode::new, Map.of(
            "x", new SubNodeData("Left","Right axis offset"),
            "y", new SubNodeData("Up","Up axis offset"),
            "z", new SubNodeData("Forward","Forward axis offset")
    )));

    /** Number */
    public static final NodeData<ComplexDoubleNumberNode> NUMBER_DOUBLE = registerNode("number", "complex_double", new NodeData.NodeDataMaker<>("Double", "A Double number", ComplexDoubleNumberNode::new, Map.of(
            "value", new SubNodeData("Value","A double number")
    )));
    public static final NodeData<ComplexIntegerNumberNode> NUMBER_INTEGER = registerNode("number", "complex_integer", new NodeData.NodeDataMaker<>("Integer", "A Integer number", ComplexIntegerNumberNode::new, Map.of(
            "value", new SubNodeData("Value","A integer number")
    )));
    public static final NodeData<DoubleValueTypeNode> VALUE_TYPE_DOUBLE = registerNode("number", "double", new NodeData.NodeDataMaker<>("Value Double", "Move the slider to select a number", DoubleValueTypeNode::new, Map.of()));
    public static final NodeData<IntegerValueTypeNode> VALUE_TYPE_INTEGER = registerNode("number", "integer", new NodeData.NodeDataMaker<>("Value Integer", "Move the slider to select a number", IntegerValueTypeNode::new, Map.of()));
    
    /** Boolean Value */
    public static final NodeData<BooleanValueTypeNode> VALUE_TYPE_BOOLEAN = registerNode("boolean_value", "boolean", new NodeData.NodeDataMaker<>("Value Boolean", "Select the value", BooleanValueTypeNode::new, Map.of()));

    /** String Value */
    public static final NodeData<StringValueTypeNode> VALUE_TYPE_STRING = registerNode("string_value", "string", new NodeData.NodeDataMaker<>("Value String", "Select the value", StringValueTypeNode::new, Map.of()));
    public static final NodeData<ComplexStringNode> STRING = registerNode("string_value", "complex_string", new NodeData.NodeDataMaker<>("String", "A string", ComplexStringNode::new, Map.of(
            "string", new SubNodeData("String","A string")
    )));


    /** Condition */
    public static final NodeData<CasterOnGroundConditionNode> CONDITION_CASTER_ON_GROUND = registerNode("condition", "caster_on_ground", new NodeData.NodeDataMaker<>("Caster On Ground", "returns true if the caster is on ground", CasterOnGroundConditionNode::new, Map.of()));


    /** Operation */
    public static final NodeData<IntegerOperationNode> OPERATION_INTEGER_NUMBER = registerNode("operation", "integer_number", new NodeData.NodeDataMaker<>("Integer Number Operation", "An operation between integer numbers", IntegerOperationNode::new, Map.of(
            "number1", new SubNodeData("First Number","the second number"),
            "operator", new SubNodeData("Operator","apply operator"),
            "number2", new SubNodeData("Second Number","the second number")
    )));
    public static final NodeData<DoubleOperationNode> OPERATION_DOUBLE_NUMBER = registerNode("operation", "double_number", new NodeData.NodeDataMaker<>("Double Number Operation", "An operation between double numbers", DoubleOperationNode::new, Map.of(
            "number1", new SubNodeData("First Number","the first number"),
            "operator", new SubNodeData("Operator","apply operator"),
            "number2", new SubNodeData("Second Number","the second number")
    )));


    /** Variable */
    public static final NodeData<VariableTypeNode> VARIABLE_TYPE = registerNode("variable", "type", new NodeData.NodeDataMaker<>("Variable Type", "Defines the type of a variable", VariableTypeNode::new, Map.of()));
    
    public static final NodeData<IntegerVariableGetterNode> VARIABLE_GETTER_INTEGER = registerNode("variable.getter", "integer", new NodeData.NodeDataMaker<>("Integer Variable", "Get a integer variable", IntegerVariableGetterNode::new, Map.of(
            "variable_name", new SubNodeData("Name","the name of the variable to modify")
    )));
    public static final NodeData<DoubleVariableGetterNode> VARIABLE_GETTER_DOUBLE = registerNode("variable.getter", "double", new NodeData.NodeDataMaker<>("Double Variable", "Get a double variable", DoubleVariableGetterNode::new, Map.of(
            "variable_name", new SubNodeData("Name","the name of the variable to modify")
    )));
    public static final NodeData<StringVariableGetterNode> VARIABLE_GETTER_STRING = registerNode("variable.getter", "string", new NodeData.NodeDataMaker<>("String Variable", "Get a string variable", StringVariableGetterNode::new, Map.of(
            "variable_name", new SubNodeData("Name","the name of the variable to modify")
    )));

    public static final NodeData<IntegerVariableModifierNode> VARIABLE_MODIFIER_INTEGER = registerNode("variable.modifier", "integer", new NodeData.NodeDataMaker<>("Integer Variable", "Modify a integer variable", IntegerVariableModifierNode::new, Map.of(
            "variable", new SubNodeData("Getter","Get the variable to modify"),
            "value", new SubNodeData("Setter","The new value to set the variable at")
    )));
    public static final NodeData<DoubleVariableModifierNode> VARIABLE_MODIFIER_DOUBLE = registerNode("variable.modifier", "double", new NodeData.NodeDataMaker<>("Double Variable", "Modify a double variable", DoubleVariableModifierNode::new, Map.of(
            "variable", new SubNodeData("Getter","Get the variable to modify"),
            "value", new SubNodeData("Setter","The new value to set the variable at")
    )));
    public static final NodeData<StringVariableModifierNode> VARIABLE_MODIFIER_STRING = registerNode("variable.modifier", "string", new NodeData.NodeDataMaker<>("String Variable", "Modify a string variable", StringVariableModifierNode::new, Map.of(
            "variable", new SubNodeData("Getter","Get the variable to modify"),
            "value", new SubNodeData("Setter","The new value to set the variable at")
    )));
    
    
    /* Operators */
    public static final NodeData<AddOperatorNode> OPERATOR_ADD = registerNode("operation.operator", "add", new NodeData.NodeDataMaker<>("Add Operator", "Adds two values to each other", AddOperatorNode::new, Map.of()));
    public static final NodeData<SubtractOperatorNode> OPERATOR_SUBTRACT = registerNode("operation.operator", "subtract", new NodeData.NodeDataMaker<>("Subtract Operator", "Subtract a values to another", SubtractOperatorNode::new, Map.of()));
    public static final NodeData<MultiplyOperatorNode> OPERATOR_MULTIPLY = registerNode("operation.operator", "multiply", new NodeData.NodeDataMaker<>("Multiply Operator", "Multiply two values", MultiplyOperatorNode::new, Map.of()));
    public static final NodeData<DivideOperatorNode> OPERATOR_DIVIDE = registerNode("operation.operator", "divide", new NodeData.NodeDataMaker<>("Divide Operator", "Divide a values for another", DivideOperatorNode::new, Map.of()));
    public static final NodeData<EqualOperatorNode> OPERATOR_EQUAL = registerNode("operation.operator", "equal", new NodeData.NodeDataMaker<>("Equal Operator", "Make a value equal to the other", EqualOperatorNode::new, Map.of()));
    public static final NodeData<GreaterThenOperatorNode> OPERATOR_GREATER_THEN = registerNode("operation.operator", "greater_then", new NodeData.NodeDataMaker<>("Greater Then Operator", "Make a value the greater between the two", GreaterThenOperatorNode::new, Map.of()));
    public static final NodeData<LessThenOperatorNode> OPERATOR_LESS_THEN = registerNode("operation.operator", "less_then", new NodeData.NodeDataMaker<>("Less Then Operator", "Make a value the smaller between the two", LessThenOperatorNode::new, Map.of()));




    // Function called to add nodes
    public static <T extends GenericNode> NodeData<T> registerNode(String groupId, String nodeId, NodeData.NodeDataMaker<T> nodeDataMaker) {
        Identifier id = Identifier.of(EnergyManipulation.MOD_ID, groupId + "." + nodeId);
        NodeData<T> newNodeData = new NodeData.Builder<T>(id, nodeDataMaker).build();
        NODES.put(id, newNodeData);
        return newNodeData;
    }
}
