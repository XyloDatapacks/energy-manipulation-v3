package com.xylo_datapacks.energy_manipulation.item.spell_book.node.instruction;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.DoubleNumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.number.NumberNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.component.TextAreaComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Consumer;

public class OutputInstructionNode extends AbstractNodeWithValue<String> implements InstructionNode {
    
    public OutputInstructionNode() {
        super(Nodes.INSTRUCTION_OUTPUT);
        setValue("");
    }
    
    public void setBounds(double minValue, double maxValue) {
        
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* InstructionNode Interface */

    @Override
    public boolean executeInstruction(SpellExecutor spellExecutor) {
        if (spellExecutor.getCaster() instanceof PlayerEntity playerEntity) {
            playerEntity.sendMessage(Text.of(getValue()), false);
            return true;
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithValue Interface */
    
    @Override
    public String getValueDisplay() {
        return getValue();
    }

    @Override
    public boolean setValue(String value) {
        return super.setValue(value != null ? value : "");
    }

    @Override
    public FlowLayout getValueSelectorComponent(UIModel model, Consumer<String> onValueChanged) {
        FlowLayout flowLayout = model.expandTemplate(FlowLayout.class, "textbox_selector_template", Map.of());
        flowLayout.childById(TextAreaComponent.class, "textbox").<TextAreaComponent>configure(text -> {
            text.onChanged().subscribe(onValueChanged::accept);
        });
        return flowLayout;
    }

    @Override
    public boolean setValueFromObject(Object value) {
        if (value instanceof String stringValue) {
            return setValue(stringValue);
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
