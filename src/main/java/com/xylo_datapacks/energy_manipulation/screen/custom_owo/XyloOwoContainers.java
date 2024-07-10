package com.xylo_datapacks.energy_manipulation.screen.custom_owo;

import io.wispforest.owo.ui.container.CollapsibleContainer;
import io.wispforest.owo.ui.core.Sizing;
import net.minecraft.text.Text;

public final class XyloOwoContainers {
    
    private XyloOwoContainers() {}

    public static CollapsibleContainerV2 collapsibleV2(Sizing horizontalSizing, Sizing verticalSizing, Text title, boolean expanded) {
        return new CollapsibleContainerV2(horizontalSizing, verticalSizing, title, expanded);
    }
}
