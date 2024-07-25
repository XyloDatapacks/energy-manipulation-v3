package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.SubNodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value.StringNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class OutputInstructionNode extends AbstractNodeWithMap implements InstructionNode {
    SubNode<StringNode> text = registerSubNode("text", SubNodes.STRING);
    
    public OutputInstructionNode() {
        super(Nodes.INSTRUCTION_OUTPUT);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        if (spellExecutor.getCaster() instanceof PlayerEntity playerEntity) {
            playerEntity.sendMessage(Text.of(text.getNode().getString(spellExecutor)), false);
            return true;
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
