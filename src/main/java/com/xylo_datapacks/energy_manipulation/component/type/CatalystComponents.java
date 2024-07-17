package com.xylo_datapacks.energy_manipulation.component.type;

import com.xylo_datapacks.energy_manipulation.component.CatalystComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

public class CatalystComponents {
    public static final CatalystComponent BASIC = new CatalystComponent.Builder()
            .impact(1)
            .directivity(1)
            .conductance(3)
            .condensationSeconds(0.4F)
            .chargeSeconds(0.5F)
            .build();
    public static final CatalystComponent SPIKE = new CatalystComponent.Builder()
            .impact(10)
            .directivity(1)
            .conductance(10)
            .condensationSeconds(0.4F)
            .chargeSeconds(2.5F)
            .usingConvertsTo(Items.BLAZE_POWDER)
            .statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.6F)
            .build();
    public static final CatalystComponent PLATE = new CatalystComponent.Builder()
            .impact(4)
            .directivity(0)
            .conductance(6)
            .condensationSeconds(0.6F)
            .chargeSeconds(5.0F)
            .usingConvertsTo(Items.BLAZE_POWDER)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 40, 1), 0.6F)
            .build();
}
