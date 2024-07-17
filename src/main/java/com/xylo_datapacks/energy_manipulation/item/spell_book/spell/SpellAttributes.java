package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.component.CatalystComponent;
import com.xylo_datapacks.energy_manipulation.component.type.CatalystComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class SpellAttributes {
    public static final String IMPACT_KEY = "impact";
    public static final String DIRECTIVITY_KEY = "directivity";
    public static final String CONDUCTANCE_KEY = "conductance";
    public static final String CONDENSATION_TIME_KEY = "condensation_time";
    public static final String CHARGE_TIME_KEY = "charge_time";
    private Integer impact;
    private Float directivity;
    private Integer conductance;
    private Integer condensationTime;
    private Integer chargeTime;

    public SpellAttributes(Integer impact, Float directivity, Integer conductance, Integer condensationTime, Integer chargeTime) {
        this.impact = impact;
        this.directivity = directivity;
        this.conductance = conductance;
        this.condensationTime = condensationTime;
        this.chargeTime = chargeTime;
    }

    public SpellAttributes(CatalystComponent catalystComponent) {
        this(catalystComponent.impact(), 
                catalystComponent.directivity(),
                catalystComponent.conductance(),
                catalystComponent.getCondensationTicks(),
                catalystComponent.getChargeTicks());
    }

    public SpellAttributes() {
    }




    public void setImpact(Integer impact) {
        this.impact = impact;
    }

    public void setDirectivity(Float directivity) {
        this.directivity = directivity;
    }

    public void setConductance(Integer conductance) {
        this.conductance = conductance;
    }

    public void setCondensationTime(Integer condensationTime) {
        this.condensationTime = condensationTime;
    }

    public void setChargeTime(Integer chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Integer getImpact() {
        return this.impact;
    }

    public Float getDirectivity() {
        return this.directivity;
    }

    public Integer getConductance() {
        return this.conductance;
    }

    public Integer getCondensationTime() {
        return this.condensationTime;
    }

    public Integer getChargeTime() {
        return this.chargeTime;
    }
    
    
    

    /** attributes are zero if not present in nbt */
    public static SpellAttributes readFromNbt(NbtCompound nbt) {
       return new SpellAttributes(
                nbt.getInt(IMPACT_KEY),
                nbt.getFloat(DIRECTIVITY_KEY),
                nbt.getInt(CONDUCTANCE_KEY),
                nbt.getInt(CONDENSATION_TIME_KEY),
                nbt.getInt(CHARGE_TIME_KEY)
       );
    }

    /** write attributes only if they are not null */
    public static NbtCompound writeToNbt(SpellAttributes attributes) {
        NbtCompound spellAttributesNbt = new NbtCompound();
        if (attributes == null) return spellAttributesNbt;

        if (attributes.impact != null) spellAttributesNbt.putInt(IMPACT_KEY, attributes.impact);
        if (attributes.directivity != null) spellAttributesNbt.putFloat(DIRECTIVITY_KEY, attributes.directivity);
        if (attributes.conductance != null) spellAttributesNbt.putInt(CONDUCTANCE_KEY, attributes.conductance);
        if (attributes.condensationTime != null) spellAttributesNbt.putInt(CONDENSATION_TIME_KEY, attributes.condensationTime);
        if (attributes.chargeTime != null) spellAttributesNbt.putInt(CHARGE_TIME_KEY, attributes.chargeTime);

        return spellAttributesNbt;
    }
}
