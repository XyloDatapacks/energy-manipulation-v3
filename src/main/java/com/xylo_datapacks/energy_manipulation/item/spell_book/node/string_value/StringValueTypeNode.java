package com.xylo_datapacks.energy_manipulation.item.spell_book.node.string_value;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import io.wispforest.owo.ui.component.TextAreaComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.function.Consumer;

public class StringValueTypeNode extends AbstractNodeWithValue<String> implements StringNode {

    public StringValueTypeNode() {
        super(Nodes.VALUE_TYPE_STRING);
        setValue("");
    }

    public void setBounds(double minValue, double maxValue) {

    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* StringNode Interface */

    @Override
    public String getString() {
        return getValue();
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
        flowLayout.childById(TextAreaComponent.class, "textbox").text(getValue()).<TextAreaComponent>configure(text -> {
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
