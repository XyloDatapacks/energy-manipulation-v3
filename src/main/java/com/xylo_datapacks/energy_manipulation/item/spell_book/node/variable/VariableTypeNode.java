package com.xylo_datapacks.energy_manipulation.item.spell_book.node.variable;

import com.xylo_datapacks.energy_manipulation.item.spell_book.node.Nodes;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record.NodeData;
import com.xylo_datapacks.energy_manipulation.screen.custom_owo.CollapsibleContainerV2;
import com.xylo_datapacks.energy_manipulation.screen.custom_owo.XyloOwoContainers;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.DiscreteSliderComponent;
import io.wispforest.owo.ui.component.DropdownComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.parsing.UIModel;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class VariableTypeNode extends AbstractNodeWithValue<Integer> {
    public static Map<Integer, VariableType> VARIABLE_TYPES = new LinkedHashMap<>() {};
    
    public VariableTypeNode() {
        super(Nodes.VARIABLE_TYPE);
        setValue(0);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* AbstractNodeWithValue Interface */

    @Override
    public String getValueDisplay() {
        return VARIABLE_TYPES.get(getValue()).getName();
    }

    @Override
    public boolean setValue(Integer value) {
        if (value >= 0 && value < VARIABLE_TYPES.size()) {
            return super.setValue(value);
        }
        return false;
    }

    @Override
    public FlowLayout getValueSelectorComponent(UIModel model, Consumer<Integer> onValueChanged) {
        FlowLayout flowLayout = model.expandTemplate(FlowLayout.class, "empty_selector_template", Map.of());

        // create layout to contain button
        CollapsibleContainerV2 collapsibleTile = ((CollapsibleContainerV2) XyloOwoContainers.collapsibleV2(Sizing.fill(100), Sizing.content(0), Text.of(""), false)
                .surface(Surface.DARK_PANEL));
        collapsibleTile.onToggled().subscribe(expanded -> {
            if (expanded) {
                collapsibleTile.contentLayoutPadding(Insets.of(0, 2, 4, 4));
            }
            else {
                collapsibleTile.contentLayoutPadding(Insets.of(0));
            }
        });
        
        LabelComponent label = Components.label(Text.of(VARIABLE_TYPES.get(this.getValue()).getName()));
        collapsibleTile.titleLayout().child(label);
        
        for (Map.Entry<Integer,VariableType> entry : VARIABLE_TYPES.entrySet()) {
            collapsibleTile.child(Components.button(Text.of(entry.getValue().getName()), buttonComponent -> {
                onValueChanged.accept(entry.getKey());
                label.text(Text.of(entry.getValue().getName()));
            }).sizing(Sizing.fill(100), Sizing.fixed(16)));
        }

        flowLayout.child(collapsibleTile);
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


    static {
        VARIABLE_TYPES.put(0, VariableType.INTEGER);
        VARIABLE_TYPES.put(1, VariableType.DOUBLE);
        VARIABLE_TYPES.put(2, VariableType.STRING);
    }
}
