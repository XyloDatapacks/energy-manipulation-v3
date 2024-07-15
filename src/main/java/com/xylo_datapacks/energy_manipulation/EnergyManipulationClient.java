package com.xylo_datapacks.energy_manipulation;


import com.xylo_datapacks.energy_manipulation.registry.ModEntityRegistry;
import com.xylo_datapacks.energy_manipulation.entity.client.ProjectileShapeEntityRenderer;
import com.xylo_datapacks.energy_manipulation.entity.client.SpellEntityRenderer;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
import com.xylo_datapacks.energy_manipulation.networking.packet.OpenSpellBookC2SPacket;
import com.xylo_datapacks.energy_manipulation.registry.ModKeyBindingRegistry;
import com.xylo_datapacks.energy_manipulation.registry.ModScreenTypeRegistry;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookHandledScreen;
import com.xylo_datapacks.energy_manipulation.util.EnergyManipulationModelPlugin;
import com.xylo_datapacks.energy_manipulation.registry.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;


public class EnergyManipulationClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        
        ModKeyBindingRegistry.init();
        registerKeyInputs();
        ModPackets.initClient();
        
        ModModelPredicateProvider.registerModModels();
        ModelLoadingPlugin.register(new EnergyManipulationModelPlugin());
        
        HandledScreens.register(ModScreenTypeRegistry.SPELL_BOOK_MENU_TYPE, SpellBookHandledScreen::new);

        /*------------------------------------------------------------------------------------ -----------------------*/
        /* register entity models */

        // spell
        EntityRendererRegistry.register(ModEntityRegistry.SPELL, SpellEntityRenderer::new);
        // projectile shape
        EntityRendererRegistry.register(ModEntityRegistry.PROJECTILE_SHAPE, ProjectileShapeEntityRenderer::new);

        /*------------------------------------------------------------------------------------ -----------------------*/
        
    }


    public static void registerKeyInputs() {

        // Register Lambda with containing key press checks
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            // Check key press
            if (ModKeyBindingRegistry.OPEN_SPELL_BOOK_MENU_KEY.isPressed() && client.player != null) {
                // call Custom Action Server RPC
                ClientPlayNetworking.send(new OpenSpellBookC2SPacket(0));
                //client.setScreen(new MyDataDrivenScreen());
            }

            // Other keys
            // ...

        });
    }
}
