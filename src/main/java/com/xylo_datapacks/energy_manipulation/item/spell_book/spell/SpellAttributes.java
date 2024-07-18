package com.xylo_datapacks.energy_manipulation.item.spell_book.spell;

import com.xylo_datapacks.energy_manipulation.component.CatalystComponent;
import net.minecraft.nbt.NbtCompound;

public class SpellAttributes {
    public static final String IMPACT_KEY = "impact";
    public static final String DIRECTIVITY_KEY = "directivity";
    public static final String CONDUCTANCE_KEY = "conductance";
    public static final String CONDENSATION_TIME_KEY = "condensation_time";
    private Integer impact;
    private Float directivity;
    private Integer conductance;
    private Integer condensationTime;

    public SpellAttributes(Integer impact, Float directivity, Integer conductance, Integer condensationTime) {
        this.impact = impact;
        this.directivity = directivity;
        this.conductance = conductance;
        this.condensationTime = condensationTime;
    }

    public SpellAttributes(CatalystComponent catalystComponent) {
        this(catalystComponent.impact(), 
                catalystComponent.directivity(),
                catalystComponent.conductance(),
                catalystComponent.getCondensationTicks());
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


    public void setFromCatalyst(CatalystComponent catalystComponents) {
        this.setImpact(catalystComponents.impact());
        this.setDirectivity(catalystComponents.directivity());
        this.setConductance(catalystComponents.conductance());
        this.setCondensationTime(catalystComponents.getCondensationTicks());
    }
    
    
    

    /** attributes are zero if not present in nbt */
    public static SpellAttributes readFromNbt(NbtCompound nbt) {
       return new SpellAttributes(
                nbt.getInt(IMPACT_KEY),
                nbt.getFloat(DIRECTIVITY_KEY),
                nbt.getInt(CONDUCTANCE_KEY),
                nbt.getInt(CONDENSATION_TIME_KEY)
       );
    }

    /** write attributes only if they are not null or zero */
    public static NbtCompound writeToNbt(SpellAttributes attributes) {
        NbtCompound spellAttributesNbt = new NbtCompound();
        if (attributes == null) return spellAttributesNbt;

        if (attributes.impact != null && attributes.impact != 0) spellAttributesNbt.putInt(IMPACT_KEY, attributes.impact);
        if (attributes.directivity != null && attributes.directivity != 0) spellAttributesNbt.putFloat(DIRECTIVITY_KEY, attributes.directivity);
        if (attributes.conductance != null && attributes.conductance != 0) spellAttributesNbt.putInt(CONDUCTANCE_KEY, attributes.conductance);
        if (attributes.condensationTime != null && attributes.condensationTime != 0) spellAttributesNbt.putInt(CONDENSATION_TIME_KEY, attributes.condensationTime);

        return spellAttributesNbt;
    }
    
}
