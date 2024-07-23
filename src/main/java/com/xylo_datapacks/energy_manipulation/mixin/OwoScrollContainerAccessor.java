package com.xylo_datapacks.energy_manipulation.mixin;

import io.wispforest.owo.ui.container.ScrollContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScrollContainer.class)
public interface OwoScrollContainerAccessor {
    @Accessor("currentScrollPosition")
    public void setCurrentScrollPosition(double currentScrollPosition);

    @Accessor("scrollOffset")
    double getScrollOffset();
}

