package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.boolean_value;

import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.SpellExecutor;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithValue;
import io.wispforest.owo.ui.component.CheckboxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;

import java.util.Map;
import java.util.function.Consumer;

public class BooleanValueTypeNode extends AbstractNodeWithValue<Boolean> implements BooleanNode {
    
    public BooleanValueTypeNode() {
        super(Nodes.VALUE_TYPE_BOOLEAN);
        setValue(true);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberNode Interface */

    @Override
    public Boolean getBoolean(SpellExecutor spellExecutor) {
        return getValue();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithValue Interface */
    
    @Override
    public String getValueDisplay() {
        return getValue().toString();
    }

    @Override
    public FlowLayout getValueSelectorComponent(UIModel model, Consumer<Boolean> onValueChanged) {
        FlowLayout flowLayout = model.expandTemplate(FlowLayout.class, "checkbox_selector_template", Map.of("checked", Boolean.toString(getValue())));
        flowLayout.childById(CheckboxComponent.class, "checkbox").onChanged(onValueChanged);
        return flowLayout;
    }

    @Override
    public boolean setValueFromObject(Object value) {
        if (value instanceof Boolean boolValue) {
            return setValue(boolValue);
        }
        if (value instanceof String stringValue) {
            return setValue(Boolean.valueOf(stringValue));
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
}
