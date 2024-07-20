package com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.record;

public enum FromNbtSettings {
    BUILD,
    SET_GUI_DATA,
    SET_EXECUTION_DATA;
    
    public boolean buildNode() {
        return this == BUILD;
    }

    public boolean setGuiData() {
        return this == BUILD || this == SET_GUI_DATA;
    }
    
    public boolean setExecutionData() {
        return this == BUILD || this == SET_EXECUTION_DATA;
    }
    
    //TODO: implement setExecutionData and setGuiData in setFromNbt methods
}
