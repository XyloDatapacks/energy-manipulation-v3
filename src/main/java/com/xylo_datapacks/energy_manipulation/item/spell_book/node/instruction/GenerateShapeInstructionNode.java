package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.RunnableNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.AbstractShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ShapeExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;

import java.util.List;
import java.util.function.Function;


public class GenerateShapeInstructionNode extends AbstractRunnableNodeWithMap<Boolean> implements InstructionNode {
    SubNode<AbstractShapeNode> shape = registerSubNode("shape", new SubNode.Builder<AbstractShapeNode>()
            .addNodeValues(List.of(
                    Nodes.SHAPE_PROJECTILE,
                    Nodes.SHAPE_RAY))
    );
    
    public GenerateShapeInstructionNode() {
        super(Nodes.INSTRUCTION_GENERATE_SHAPE);
    }
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    /** @return true if generated a shape */
    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        return runNode(spellExecutor);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/


    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractRunnableNodeWithMap Interface */

    @Override
    public Boolean newExecution(SpellExecutor spellExecutor) {
        Boolean didGenerate = execute(shape).generateShape(spellExecutor);
        
        if (shouldReset(spellExecutor)) {
            resetExecution(spellExecutor);
        }
        return didGenerate;
    }

    @Override
    public Boolean resumeExecution(SpellExecutor spellExecutor) {
        
        if (shouldReset(spellExecutor)) {
            resetExecution(spellExecutor);
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
