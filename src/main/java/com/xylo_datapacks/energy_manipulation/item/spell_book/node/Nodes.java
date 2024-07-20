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
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.position.AlignPositionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.position.OffsetPositionNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.SubNodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ProjectileShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.RayShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.boolean_value.BooleanValueTypeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleValueTypeNode;
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
            "x", new SubNodeData("Right","Right axis offset"),
            "y", new SubNodeData("Up","Up axis offset"),
            "z", new SubNodeData("Forward","Forward axis offset")
    )));

    /** Number */
    public static final NodeData<ComplexDoubleNumberNode> NUMBER_DOUBLE = registerNode("number", "double", new NodeData.NodeDataMaker<>("Double", "A Double number", ComplexDoubleNumberNode::new, Map.of(
            "value", new SubNodeData("Value","A double number")
    )));
    public static final NodeData<ComplexIntegerNumberNode> NUMBER_INTEGER = registerNode("number", "integer", new NodeData.NodeDataMaker<>("Integer", "A Integer number", ComplexIntegerNumberNode::new, Map.of(
            "value", new SubNodeData("Value","A integer number")
    )));
    public static final NodeData<DoubleValueTypeNode> VALUE_TYPE_DOUBLE = registerNode("number_value", "double", new NodeData.NodeDataMaker<>("Value Double", "Move the slider to select a number", DoubleValueTypeNode::new, Map.of()));
    public static final NodeData<IntegerValueTypeNode> VALUE_TYPE_INTEGER = registerNode("number_value", "integer", new NodeData.NodeDataMaker<>("Value Integer", "Move the slider to select a number", IntegerValueTypeNode::new, Map.of()));
    
    /** Boolean Value */
    public static final NodeData<BooleanValueTypeNode> VALUE_TYPE_BOOLEAN = registerNode("boolean_value", "boolean", new NodeData.NodeDataMaker<>("Value Boolean", "Select the value", BooleanValueTypeNode::new, Map.of()));
    
    /** Condition */
    public static final NodeData<CasterOnGroundConditionNode> CONDITION_CASTER_ON_GROUND = registerNode("condition", "caster_on_ground", new NodeData.NodeDataMaker<>("Caster On Ground", "returns true if the caster is on ground", CasterOnGroundConditionNode::new, Map.of()));


    // Function called to add nodes
    public static <T extends GenericNode> NodeData<T> registerNode(String groupId, String nodeId, NodeData.NodeDataMaker<T> nodeDataMaker) {
        Identifier id = Identifier.of(EnergyManipulation.MOD_ID, groupId + "." + nodeId);
        NodeData<T> newNodeData = new NodeData.Builder<T>(id, nodeDataMaker).build();
        NODES.put(id, newNodeData);
        return newNodeData;
    }
}
