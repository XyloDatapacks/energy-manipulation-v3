package com.xylo_datapacks.energy_manipulation.item.spell_book.node.number;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.spell_book.spell.SpellExecutor;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;

import java.util.Map;
import java.util.function.Consumer;

public class IntegerValueTypeNode extends AbstractNodeWithValue<Integer> implements NumberNode {
    private int minValue = -100;
    private int maxValue = 100;
    
    public IntegerValueTypeNode() {
        super(Nodes.VALUE_TYPE_INTEGER);
        setValue(0);
    }
    
    public void setBounds(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* NumberNode Interface */

    @Override
    public Number getNumber(SpellExecutor spellExecutor) {
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
    public boolean setValue(Integer value) {
        if (value >= minValue && value <= maxValue) {
            return super.setValue(value);
        }
        return false;
    }

    @Override
    public FlowLayout getValueSelectorComponent(UIModel model, Consumer<Integer> onValueChanged) {
        FlowLayout flowLayout = model.expandTemplate(FlowLayout.class, "slider_selector_template", Map.of("value", Integer.toString(getValue()), "min_value", Integer.toString(minValue), "max_value", Integer.toString(maxValue)));
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
