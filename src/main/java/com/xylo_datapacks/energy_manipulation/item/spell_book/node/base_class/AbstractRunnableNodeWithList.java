package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRunnableNodeWithList<T extends GenericNode, U> extends AbstractNodeWithList<T> implements RunnableNode<U> {
    protected static final String LAST_EXECUTED_KEY = "last_executed";
    @Nullable private Integer lastExecutedIndex;
    
    public AbstractRunnableNodeWithList(NodeData<?> nodeData, String subNodesId, SubNode.Builder<T> subNodeBuilderTemplate) {
        super(nodeData, subNodesId, subNodeBuilderTemplate);
    }
    
    protected void setLastExecuted(Integer index) {
        lastExecutedIndex = index;
    }
    
    protected int getLastExecutedIndex() {
        return lastExecutedIndex != null ? lastExecutedIndex : -1;
    }

    protected Optional<T> execute(int index) {
        SubNode<T> subNode = getSubNode(index);
        if (subNode == null) return Optional.empty();
        
        setLastExecuted(index);
        return Optional.of(subNode.getNode());
    }

    protected <R> Optional<R> execute(int index, Function<T, R> action) {
        return execute(index).map(action);
    }

    @Override
    public U runNode(SpellExecutor spellExecutor) {
        if (isFreshExecution()) {
            return newExecution(spellExecutor);
        }

        SubNode<T> subNode = getSubNode(getLastExecutedIndex());
        if (subNode != null && subNode.getNode() instanceof RunnableNode<?> runnableNode) {
            runnableNode.runNode(spellExecutor);
        }
        return resumeExecution(spellExecutor);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    @Override
    public boolean isFreshExecution() {
        return lastExecutedIndex == null || lastExecutedIndex == -1;
    }

    @Override
    public void resetExecution(SpellExecutor spellExecutor) {
        lastExecutedIndex = null;
    }

    @Override
    public void saveExecutionToNbt(NbtCompound nbt) {
        if (lastExecutedIndex != null && lastExecutedIndex != -1) nbt.putInt(LAST_EXECUTED_KEY, lastExecutedIndex);
    }

    @Override
    public void loadExecutionFromNbt(NbtCompound nbt) {
        lastExecutedIndex = nbt.contains(LAST_EXECUTED_KEY) ? nbt.getInt(LAST_EXECUTED_KEY) : -1;
    }

    /*----------------------------------------------------------------------------------------------------------------*/


    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithList Interface */

    @Override
    public NbtCompound toNbt() {
        NbtCompound nbt = super.toNbt();
        if (!isFreshExecution()) {
            saveExecutionToNbt(nbt);
        }
        return nbt;
    }
    
    @Override
    public GenericNode setFromNbt(NbtCompound nbt) {
        loadExecutionFromNbt(nbt);
        return super.setFromNbt(nbt);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
