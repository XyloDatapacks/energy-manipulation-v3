package com.xylo_datapacks.energy_manipulation.config;

import com.xylo_datapacks.energy_manipulation.mixin.SoundEventAccessor;
import net.minecraft.sound.SoundEvent;

/**
 * Stores information about a single spellBook.
 * A spellBook has a name (which is used in registry as name + _spellBook), row width, and number of rows.
 * Row width represents how long a row is, while number of rows is used for the number of horizontal slot groups.
 *
 * SpellBookInfo instances are automatically read from the config file and used in {@link ...}.
 */
public class SpellBookInfo {

    private final String name;
    private final int rowWidth;
    private final int numberOfRows;
    private final boolean isFireImmune;
    private final boolean dyeable;
    private String openSound;
    // TODO: add various charge sounds and cast sound

    /**
     * Creates a new SpellBookInfo instance.
     *
     * @param name  name of this spellBook. Used in registry as "name + _spellBook".
     * @param rowWidth  width of each row
     * @param numberOfRows  number of horizontal slot groups
     * @param isFireImmune  whether this spellBook is fire-immune (Netherite)
     */
    public SpellBookInfo(String name, int rowWidth, int numberOfRows, boolean isFireImmune, String openSound) {
        this.name = name;
        this.rowWidth = rowWidth;
        this.numberOfRows = numberOfRows;
        this.isFireImmune = isFireImmune;
        this.openSound = openSound;
        this.dyeable = false;
    }

    public SpellBookInfo(String name, int rowWidth, int numberOfRows, boolean isFireImmune, String openSound, boolean dyeable) {
        this.name = name;
        this.rowWidth = rowWidth;
        this.numberOfRows = numberOfRows;
        this.isFireImmune = isFireImmune;
        this.openSound = openSound;
        this.dyeable = dyeable;
    }

    public String getName() {
        return name;
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public boolean isFireImmune() {
        return isFireImmune;
    }

    public String getOpenSound() {
        return openSound;
    }

    public void setOpenSound(String sound) {
        this.openSound = sound;
    }

    public boolean isDyeable() {
        return dyeable;
    }

    /**
     * Creates a new SpellBookInfo instance.
     *
     * @param name  name of this spellBook. Used in registry as "name + _spellBook".
     * @param rowWidth  width of each row
     * @param numberOfRows  number of horizontal slot groups
     */
    public static SpellBookInfo of(String name, int rowWidth, int numberOfRows, boolean isFireImmune, SoundEvent openSound) {
        return new SpellBookInfo(name, rowWidth, numberOfRows, isFireImmune, ((SoundEventAccessor) openSound).getId().toString());
    }

    public static SpellBookInfo of(String name, int rowWidth, int numberOfRows, boolean isFireImmune, SoundEvent openSound, boolean dyeable) {
        return new SpellBookInfo(name, rowWidth, numberOfRows, isFireImmune, ((SoundEventAccessor) openSound).getId().toString(), dyeable);
    }
}