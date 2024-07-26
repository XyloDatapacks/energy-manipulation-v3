package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.FromNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.ToNbtSettings;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractRunnableNodeWithMap<U> extends AbstractNodeWithMap implements RunnableNode<U> {
    protected static final String LAST_EXECUTED_KEY = "last_executed";
    @Nullable private String lastExecutedId;
    
    public AbstractRunnableNodeWithMap(NodeData<?> nodeData) {
        super(nodeData);
    }

    protected void setLastExecuted(String id) {
        lastExecutedId = id;
    }
    
    protected String getLastExecuted() {
        return lastExecutedId != null ? lastExecutedId : "";
    }

    protected <T extends RunnableNode<?>> T execute(SubNode<T> subNode) {
        setLastExecuted(subNode.getId());
        return subNode.getNode();
    }

    protected <T extends RunnableNode<?>, R> R execute(SubNode<T> subNode, Function<T, R> action) {
        return action.apply(execute(subNode));
    }

    @Override
    public U runNode(SpellExecutor spellExecutor) {
        if (isFreshExecution()) {
            return newExecution(spellExecutor);
        }

        SubNode<? extends GenericNode> subNode = getSubNode(getLastExecuted());
        if (subNode != null && subNode.getNode() instanceof RunnableNode<?> runnableNode) {
            runnableNode.runNode(spellExecutor);
        }
        return resumeExecution(spellExecutor);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* RunnableNode Interface */

    @Override
    public boolean isFreshExecution() {
        return lastExecutedId == null || lastExecutedId.isEmpty();
    }

    @Override
    public void resetExecution(SpellExecutor spellExecutor) {
        lastExecutedId = null;
    }

    @Override
    public void saveExecutionToNbt(NbtCompound nbt) {
        if (lastExecutedId != null && !lastExecutedId.isEmpty()) nbt.putString(LAST_EXECUTED_KEY, lastExecutedId);
    }

    @Override
    public void loadExecutionFromNbt(NbtCompound nbt) {
        lastExecutedId = nbt.getString(LAST_EXECUTED_KEY);
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithMap Interface */

    @Override
    public NbtCompound toNbt(ToNbtSettings settings) {
        NbtCompound nbt = super.toNbt(settings);
        if (settings.saveExecutionData() && !isFreshExecution()) {
            saveExecutionToNbt(nbt);
        }
        return nbt;
    }
    
    @Override
    public GenericNode setFromNbt(NbtCompound nbt, FromNbtSettings settings) {
        loadExecutionFromNbt(nbt);
        return super.setFromNbt(nbt, settings);
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
