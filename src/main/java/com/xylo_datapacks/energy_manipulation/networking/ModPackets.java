package com.xylo_datapacks.energy_manipulation.networking;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.networking.packet.OpenSpellBookC2SPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier OPEN_SPELL_BOOK_MENU_ID = new Identifier(EnergyManipulation.MOD_ID, "open_spell_book");
    
    // Register Server RPCs receiver
    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(OPEN_SPELL_BOOK_MENU_ID, OpenSpellBookC2SPacket::receive);
        
        // register other packets
        // ...
    }

    // Register Client RPCs receiver
    public static void registerS2CPackets() {

        // register other packets
        // ...
    }
}
