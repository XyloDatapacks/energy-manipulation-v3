package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

public abstract class AbstractRunnableNodeWithList<T extends GenericNode> extends AbstractNodeWithList<T> implements RunnableNode {
    private int lastExecutedIndex;
    
    public AbstractRunnableNodeWithList(NodeData<?> nodeData, String subNodesId, SubNode.Builder<T> subNodeBuilderTemplate) {
        super(nodeData, subNodesId, subNodeBuilderTemplate);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithList Interface */

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
        SubNode<T> subNode = getSubNode(lastExecutedIndex);
        if (subNode != null && subNode.getNode() instanceof RunnableNode runnableNode) {
            runnableNode.resumeExecution(spellExecutor);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
