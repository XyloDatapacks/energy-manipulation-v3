package com.xylo_datapacks.energy_manipulation.event;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    // Categories
    public static final String KEY_CATEGORY_TUTORIAL = "key.category." + EnergyManipulation.MOD_ID;
    
    // Keybindings
    public static final String KEY_OPEN_SPELL_BOOK_MENU = "key." + EnergyManipulation.MOD_ID + ".spell_book.open_menu";
    public static KeyBinding openSpellBookMenuKey;

    /**
     * Has to be called in onInitializeClient function of your ClientModInitializer class
     */
    public static void registerKeyBindings() {
        
        // Register Keybindings
        openSpellBookMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_SPELL_BOOK_MENU, 
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KEY_CATEGORY_TUTORIAL
        ));
        
        // Add other Keybindings
        // ...
        
        // register lambda for key press checks
        registerKeyInputs();
    }

    public static void registerKeyInputs() {

        // Register Lambda with containing key press checks
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            // Check key press
            if (openSpellBookMenuKey.isPressed() && client.player != null) {
                // call Custom Action Server RPC
                ClientPlayNetworking.send(ModPackets.OPEN_SPELL_BOOK_MENU_ID, PacketByteBufs.create());
                //client.setScreen(new MyDataDrivenScreen());
            }

            // Other keys
            // ...

        });
    }
}
