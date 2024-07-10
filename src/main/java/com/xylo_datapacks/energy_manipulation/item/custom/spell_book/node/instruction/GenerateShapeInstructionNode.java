package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.shape.ShapeNode;

import java.util.List;


public class GenerateShapeInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<ShapeNode> shape = registerSubNode("shape", new SubNode.Builder<ShapeNode>()
            .addNodeValues(List.of(
                    Nodes.SHAPE_PROJECTILE,
                    Nodes.SHAPE_RAY))
            .build(this));
    
    public GenerateShapeInstructionNode() {
        super(Nodes.INSTRUCTION_GENERATE_SHAPE);
    }
    

/*--------------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return shape.getNode().generateShape(spellExecutor);
    }

/*--------------------------------------------------------------------------------------------------------------------*/



}
