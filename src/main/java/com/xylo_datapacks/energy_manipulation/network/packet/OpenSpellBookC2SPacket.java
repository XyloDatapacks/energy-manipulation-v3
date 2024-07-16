package com.xylo_datapacks.energy_manipulation.network.packet;

import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.network.ModPackets;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;

// it is a server RPC
public record OpenSpellBookC2SPacket(Integer integer) implements CustomPayload{
    public static final CustomPayload.Id<OpenSpellBookC2SPacket> ID = new CustomPayload.Id<>(ModPackets.OPEN_SPELL_BOOK_MENU_PACKET_ID);
    public static final PacketCodec<ByteBuf, OpenSpellBookC2SPacket> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, OpenSpellBookC2SPacket::integer, OpenSpellBookC2SPacket::new);;
    
    public static void receive(OpenSpellBookC2SPacket payload, ServerPlayNetworking.Context player) {
        
        ServerPlayerEntity serverPlayer = player.player();
        if (serverPlayer.getMainHandStack().getItem() instanceof SpellBookItem spellBook) {
            SpellBookItem.openScreen(serverPlayer, serverPlayer.getMainHandStack());
        }
        else if (serverPlayer.getOffHandStack().getItem() instanceof SpellBookItem spellBook) {
            SpellBookItem.openScreen(serverPlayer, serverPlayer.getOffHandStack());
        }
    }
    
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

