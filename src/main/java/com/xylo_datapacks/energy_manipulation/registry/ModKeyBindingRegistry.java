package com.xylo_datapacks.energy_manipulation.registry;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindingRegistry {
    public static final String KEY_CATEGORY_TUTORIAL = "key.category." + EnergyManipulation.MOD_ID;

    // Keybindings
    public static KeyBinding OPEN_SPELL_BOOK_MENU_KEY = registerKey(
            "key." + EnergyManipulation.MOD_ID + ".spell_book.open_menu", GLFW.GLFW_KEY_G);
    
    
    public static KeyBinding registerKey(String translationKey, int code) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                translationKey,
                InputUtil.Type.KEYSYM,
                code,
                KEY_CATEGORY_TUTORIAL));
    }
    
    public static void init()
    {
        EnergyManipulation.LOGGER.info("Registering Mod keybindings for " + EnergyManipulation.MOD_ID);
    }
}
