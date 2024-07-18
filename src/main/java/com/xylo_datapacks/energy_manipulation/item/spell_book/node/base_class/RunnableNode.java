package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class;

import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Supplier;

public interface RunnableNode<U> extends GenericNode {

    /** starts fresh execution 
     * </p> override to implement functionality */
    public abstract U newExecution(SpellExecutor spellExecutor);
    
    /** continue execution from where it stopped.
     * </p> override to add extra logic, like continue running instructions for an 
     * instruction provider or continue a loop from where it stopped */
    public abstract U resumeExecution(SpellExecutor spellExecutor);
    
    /** determine whether it is a fresh execution or not 
     * </p> override to add extra conditions */
    public abstract boolean isFreshExecution();
    
    /** reset all the execution status variables 
     * </p> override to reset extra data */
    public abstract void resetExecution(SpellExecutor spellExecutor);
    
    /** add execution data to nbt 
     * </p> override to add extra data */
    public abstract void saveExecutionToNbt(NbtCompound nbt);

    /** load execution data from nbt 
     * </p> override to add extra data */
    public abstract void loadExecutionFromNbt(NbtCompound nbt);
}
