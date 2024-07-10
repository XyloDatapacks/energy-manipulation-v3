package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class;

import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.parsing.UIModel;

import java.util.function.Consumer;

public interface ValueTypeNode<T> extends GenericNode {
    
    public abstract T getValue();

    public abstract String getValueDisplay();

    public abstract boolean setValue(T value);
    
    public abstract FlowLayout getValueSelectorComponent(UIModel model, Consumer<T> onValueChanged);
    
    public abstract boolean setValueFromObject(Object value);
}
