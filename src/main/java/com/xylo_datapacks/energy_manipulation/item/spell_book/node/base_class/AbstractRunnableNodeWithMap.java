package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public abstract class AbstractRunnableNodeWithMap extends AbstractNodeWithMap implements RunnableNode {
    private String lastExecutedId;
    
    public AbstractRunnableNodeWithMap(NodeData<?> nodeData) {
        super(nodeData);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithMap Interface */

    @Override
    public NbtCompound toNbt() {
        return super.toNbt();
    }

    @Override
    public GenericNode setFromNbt(NbtCompound nbt) {
        return super.setFromNbt(nbt);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    @Override
    public void resumeExecution(SpellExecutor spellExecutor) {
        SubNode<? extends GenericNode> subNode = getSubNode(lastExecutedId);
        if (subNode != null && subNode.getNode() instanceof RunnableNode runnableNode) {
            runnableNode.resumeExecution(spellExecutor);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
