package com.xylo_datapacks.energy_manipulation.networking.packet;

import com.xylo_datapacks.energy_manipulation.item.custom.SpellBookItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// it is a server RPC
public class OpenSpellBookC2SPacket {
    
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        
        if (player.getMainHandStack().getItem() instanceof SpellBookItem spellBook) {
            SpellBookItem.openScreen(player, player.getMainHandStack());
        }
        else if (player.getOffHandStack().getItem() instanceof SpellBookItem spellBook) {
            SpellBookItem.openScreen(player, player.getOffHandStack());
        }
    }

}

