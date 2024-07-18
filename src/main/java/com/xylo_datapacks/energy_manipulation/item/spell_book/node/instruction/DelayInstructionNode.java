package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.*;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape.ShapeNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.util.Identifier;

import java.util.List;

public class DelayInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<NumberNode> time = registerSubNode("time", new SubNode.Builder<NumberNode>()
            .addNodeValues(List.of(
                    Nodes.NUMBER_INTEGER,
                    Nodes.VALUE_TYPE_INTEGER))
    );
    
    public DelayInstructionNode() {
        super(Nodes.INSTRUCTION_DELAY);
    }
    
    
    /*--------------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        spellExecutor.setDelayTicks((int) time.getNode().getNumber(spellExecutor));
        System.out.println("delay of " + time.getNode().getNumber(spellExecutor));
        spellExecutor.getExecutionData().returnType = ReturnType.RETURN;
        return true;
    }

    /*--------------------------------------------------------------------------------------------------------------------*/
    
}
