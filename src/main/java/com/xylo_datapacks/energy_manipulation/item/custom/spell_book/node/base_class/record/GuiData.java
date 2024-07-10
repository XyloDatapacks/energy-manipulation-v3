package com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record;

import net.minecraft.nbt.NbtCompound;

public class GuiData {
    private boolean expanded;
    
    public GuiData() {
        expanded = true;
    }

    public boolean getExpanded() {
        return expanded;
    }
    
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    
    public void toggleExpanded() {
        setExpanded(!getExpanded());
    }

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();
        tag.putBoolean("expanded", expanded);
        return tag;
    }
    
    public GuiData setFromNbt(NbtCompound tag) {
        this.expanded = tag.getBoolean("expanded");
        return this;
    }
}
