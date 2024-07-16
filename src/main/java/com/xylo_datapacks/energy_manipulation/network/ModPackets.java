package com.xylo_datapacks.energy_manipulation.network;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.network.packet.OpenSpellBookC2SPacket;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier INIT_SPELL_BOOK_MENU_PACKET_ID = EnergyManipulation.id("init_spell_book_menu");
    public static final Identifier OPEN_SPELL_BOOK_MENU_PACKET_ID = EnergyManipulation.id("open_spell_book_menu");

    
    public static void initClient() {
        // register other packets
        // ...
    }
    
    public static void initServer() {
        PayloadTypeRegistry.playC2S().register(OpenSpellBookC2SPacket.ID, OpenSpellBookC2SPacket.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(OpenSpellBookC2SPacket.ID, OpenSpellBookC2SPacket::receive);
        // register other packets
        // ...
    }
}
