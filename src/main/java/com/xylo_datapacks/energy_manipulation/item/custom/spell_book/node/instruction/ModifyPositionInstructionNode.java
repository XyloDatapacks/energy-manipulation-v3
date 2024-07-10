package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.position.PositionNode;

import java.util.List;


public class ModifyPositionInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<PositionNode> position = registerSubNode("position", new SubNode.Builder<PositionNode>()
            .addNodeValues(List.of(
                    Nodes.POSITION_OFFSET,
                    Nodes.POSITION_ALIGN))
            .build(this));
    
    public ModifyPositionInstructionNode() {
        super(Nodes.INSTRUCTION_MODIFY_POSITION);
    }

/*--------------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */
    
    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        spellExecutor.setContextPosition(position.getNode().getPosition(spellExecutor, spellExecutor.getContextPosition())); 
        return true;
    }

/*--------------------------------------------------------------------------------------------------------------------*/
    
    
}
