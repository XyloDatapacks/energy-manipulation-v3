package com.xylo_datapacks.energy_manipulation.component;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.dynamic.Codecs;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {

    public static final ComponentType<Integer> TEST_COMPONENT = registerComponent("test_component", builder -> builder.codec(Codecs.NONNEGATIVE_INT).packetCodec(PacketCodecs.VAR_INT));

    public static final ComponentType<CatalystComponent> CATALYST = registerComponent("catalyst", builder -> builder.codec(CatalystComponent.CODEC).packetCodec(CatalystComponent.PACKET_CODEC).cache());
    
    private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, EnergyManipulation.id(id), ((ComponentType.Builder<T>)builderOperator.apply(ComponentType.builder())).build());
    }

    public static void init() {}
}
