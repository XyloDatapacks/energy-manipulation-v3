package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record;

public enum ToNbtSettings {
    EDITOR,
    EXECUTION,
    EXECUTION_ONLY;
    
    public boolean saveGuiData() {
        return this == EDITOR;
    }

    public boolean saveExecutionData() {
        return this == EXECUTION || this == EXECUTION_ONLY;
    }
    
    public boolean saveOnlyExecutionData() {
        return this == EXECUTION_ONLY;
    }
}
