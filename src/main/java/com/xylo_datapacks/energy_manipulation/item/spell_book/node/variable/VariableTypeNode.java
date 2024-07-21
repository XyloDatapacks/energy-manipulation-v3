package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;

import java.util.Map;
import java.util.function.Consumer;

public class VariableTypeNode extends AbstractNodeWithValue<Integer> {
    // TODO: add actual selection using dropdown 
    
    public VariableTypeNode() {
        super(Nodes.VARIABLE_TYPE);
        setValue(0);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithValue Interface */

    @Override
    public String getValueDisplay() {
        return getValue().toString();
    }

    @Override
    public boolean setValue(Integer value) {
        if (value >= 0 && value <= 5) {
            return super.setValue(value);
        }
        return false;
    }

    @Override
    public FlowLayout getValueSelectorComponent(UIModel model, Consumer<Integer> onValueChanged) {
        FlowLayout flowLayout = model.expandTemplate(FlowLayout.class, "slider_selector_template", Map.of("value", Double.toString(getValue()), "min_value", Double.toString(0), "max_value", Double.toString(5)));
        flowLayout.childById(DiscreteSliderComponent.class, "slider").<DiscreteSliderComponent>configure(slider -> {
            slider.onChanged().subscribe(value -> onValueChanged.accept((int) value));
        });
        return flowLayout;
    }

    @Override
    public boolean setValueFromObject(Object value) {
        if (value instanceof Integer intValue) {
            return setValue(intValue) ;
        }
        if (value instanceof String stringValue) {
            return setValue(Integer.valueOf(stringValue));
        }
        return false;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
}
