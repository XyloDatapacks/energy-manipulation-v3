package com.xylo_datapacks.energy_manipulation.component;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.xylo_datapacks.energy_manipulation.network.codec.ModPacketCodec;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.List;
import java.util.Optional;

public record CatalystComponent(double impact, float directivity, double conductance, float condensationSeconds, float chargeSeconds, Optional<ItemStack> usingConvertsTo, List<CatalystComponent.StatusEffectEntry> effects) {
    private static final float DEFAULT_CHARGE_SECONDS = 1.6F;
    public static final Codec<CatalystComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.DOUBLE.fieldOf("impact").forGetter(CatalystComponent::impact),
                            Codec.FLOAT.fieldOf("directivity").forGetter(CatalystComponent::directivity),
                            Codec.DOUBLE.fieldOf("conductance").forGetter(CatalystComponent::conductance),
                            Codec.FLOAT.fieldOf("condensationSeconds").forGetter(CatalystComponent::condensationSeconds),
                            Codec.FLOAT.fieldOf("chargeSeconds").forGetter(CatalystComponent::chargeSeconds),
                            ItemStack.UNCOUNTED_CODEC.optionalFieldOf("using_converts_to").forGetter(CatalystComponent::usingConvertsTo),
                            CatalystComponent.StatusEffectEntry.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(CatalystComponent::effects)
                    )
                    .apply(instance, CatalystComponent::new)
    );
    public static final PacketCodec<RegistryByteBuf, CatalystComponent> PACKET_CODEC = ModPacketCodec.tuple(
            PacketCodecs.DOUBLE, CatalystComponent::impact,
            PacketCodecs.FLOAT, CatalystComponent::directivity,
            PacketCodecs.DOUBLE, CatalystComponent::conductance,
            PacketCodecs.FLOAT, CatalystComponent::condensationSeconds,
            PacketCodecs.FLOAT, CatalystComponent::chargeSeconds,
            ItemStack.PACKET_CODEC.collect(PacketCodecs::optional), CatalystComponent::usingConvertsTo,
            CatalystComponent.StatusEffectEntry.PACKET_CODEC.collect(PacketCodecs.toList()), CatalystComponent::effects,
            CatalystComponent::new
    );

    public int getChargeTicks() {
        return (int)(this.chargeSeconds * 20.0F);
    }
    
    /** if the item stack is not empty, give item and return the same item stack. else return the converted stack */
    public static ItemStack convertTo(PlayerEntity player, ItemStack itemStack, CatalystComponent catalystComponent) {
        Optional<ItemStack> optional = catalystComponent.usingConvertsTo();
        if (optional.isPresent() && !player.isInCreativeMode()) {
            
            if (itemStack.isEmpty()) {
                return ((ItemStack)optional.get()).copy();
            }

            if (!player.getWorld().isClient()) {
                player.getInventory().insertStack(((ItemStack)optional.get()).copy());
            }
        }
        return itemStack;
    }

    /** if the item stack is not empty, give item and return the same item stack. else return the converted stack */
    public static void giveConversionItem(PlayerEntity player, CatalystComponent catalystComponent) {
        Optional<ItemStack> optional = catalystComponent.usingConvertsTo();
        if (optional.isPresent() && !player.isInCreativeMode()) {
            
            if (!player.getWorld().isClient()) {
                player.getInventory().insertStack(((ItemStack)optional.get()).copy());
            }
        }
    }
    

    public static void applyEffects(PlayerEntity player, ItemStack itemStack, CatalystComponent catalystComponent) {
        if (!player.getWorld().isClient()) {
            for (CatalystComponent.StatusEffectEntry statusEffectEntry : catalystComponent.effects()) {
                if (player.getRandom().nextFloat() < statusEffectEntry.probability()) {
                    player.addStatusEffect(statusEffectEntry.effect());
                }
            }
        }
    }
    
    
    

    public static class Builder {
        double impact;
        float directivity; 
        double conductance; 
        float condensationSeconds;
        float chargeSeconds = DEFAULT_CHARGE_SECONDS;
        private Optional<ItemStack> usingConvertsTo = Optional.empty();
        private final ImmutableList.Builder<CatalystComponent.StatusEffectEntry> effects = ImmutableList.builder();

        public Builder impact(double impact) {
            this.impact = impact;
            return this;
        }
        
        public Builder directivity(float directivity) {
            this.directivity = directivity;
            return this;
        }
        
        public Builder conductance(double conductance) {
            this.conductance = conductance;
            return this;
        }
        
        public Builder condensationSeconds(float condensationSeconds) {
            this.condensationSeconds = condensationSeconds;
            return this;
        }
        
        public Builder chargeSeconds(float chargeSeconds) {
            this.chargeSeconds = chargeSeconds;
            return this;
        }

        /**
         * Specifies a status effect to apply to an entity when a catalyst item is consumed.
         * This method may be called multiple times to apply several status effects when catalyst is consumed.
         *
         * @param chance the chance the status effect is applied, on a scale of {@code 0.0F} to {@code 1.0F}
         * @param effect the effect instance to apply
         */
        public Builder statusEffect(StatusEffectInstance effect, float chance) {
            this.effects.add(new CatalystComponent.StatusEffectEntry(effect, chance));
            return this;
        }

        public Builder usingConvertsTo(ItemConvertible item) {
            this.usingConvertsTo = Optional.of(new ItemStack(item));
            return this;
        }

        public CatalystComponent build() {
            return new CatalystComponent(this.impact, this.directivity, this.conductance, this.condensationSeconds, this.chargeSeconds, this.usingConvertsTo, this.effects.build());
        }
    }

    public static record StatusEffectEntry(StatusEffectInstance effect, float probability) {
        public static final Codec<CatalystComponent.StatusEffectEntry> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                StatusEffectInstance.CODEC.fieldOf("effect").forGetter(CatalystComponent.StatusEffectEntry::effect),
                                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("probability", 1.0F).forGetter(CatalystComponent.StatusEffectEntry::probability)
                        )
                        .apply(instance, CatalystComponent.StatusEffectEntry::new)
        );
        public static final PacketCodec<RegistryByteBuf, CatalystComponent.StatusEffectEntry> PACKET_CODEC = PacketCodec.tuple(
                StatusEffectInstance.PACKET_CODEC,
                CatalystComponent.StatusEffectEntry::effect,
                PacketCodecs.FLOAT,
                CatalystComponent.StatusEffectEntry::probability,
                CatalystComponent.StatusEffectEntry::new
        );

        public StatusEffectInstance effect() {
            return new StatusEffectInstance(this.effect);
        }
    }
}
