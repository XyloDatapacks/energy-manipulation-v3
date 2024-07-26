package com.xylo_datapacks.energy_manipulation.item.spell_book.node.shape;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractRunnableNodeWithMap;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.RunnableNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.SubNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ReturnType;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.ShapeExecutor;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;

import java.util.function.Function;

/**
 * the sub nodes are meant to be run directly from this node, by using .execute(), or by manually setting this.setLastExecuted().
 * this way if we generate another shape, then running the rootNode will bring the execution to that shape's creation node.
 * after calling any subNode is imperative to set the last executed back to "dummy", else this shape will not be able to 
 * retrive the shapeNode from Nbt.
 * <p>
 * example of call of a tick node from shape entity. (shapeNode is the ShapeNode that generated the entity):
 * shapeNode.execute(tick).runInstructions(this);
 * shapeNode.setLastExecuted("dummy");
 */
public abstract class AbstractShapeNode extends AbstractRunnableNodeWithMap<Boolean> implements ShapeNode {
    
    public AbstractShapeNode(NodeData<?> nodeData) {
        super(nodeData);
    }
    
    
    public abstract void summonShape(SpellExecutor spellExecutor);
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* ShapeNode Interface */
    
    @Override
    public boolean generateShape(SpellExecutor spellExecutor) {
        return runNode(spellExecutor);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractRunnableNodeWithMap Interface */
    
    @Override
    public <T extends RunnableNode<?>> T execute(SubNode<T> subNode) {
        return super.execute(subNode);
    }

    @Override
    public <T extends RunnableNode<?>, R> R execute(SubNode<T> subNode, Function<T, R> action) {
        return super.execute(subNode, action);
    }

    @Override
    public Boolean newExecution(SpellExecutor spellExecutor) {
        // block execution so that new shape will result stuck here
        spellExecutor.getExecutionData().returnType = ReturnType.RETURN;
        setLastExecuted("dummy");
        
        // create shape copying all data from spellExecutor
        summonShape(spellExecutor);

        // reset return type so that this executor goes on
        spellExecutor.getExecutionData().returnType = ReturnType.NONE;
        resetExecution(spellExecutor);
        return true;
    }

    @Override
    public Boolean resumeExecution(SpellExecutor spellExecutor) {
        
        // if we are running stuck on dummy, we want to set this node as spell executor and interrupt execution
        if (getLastExecuted().equals("dummy") && spellExecutor instanceof ShapeExecutor shapeExecutor) {
            shapeExecutor.setShapeNode(this);
            spellExecutor.getExecutionData().returnType = ReturnType.RETURN;
            return false;
        }
        
        // if we are not on dummy we just let things go.
        // we never reset the execution here 
        
        return true;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
