package com.xylo_datapacks.energy_manipulation.mixin;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    /**@Invoker("getUsingItemHandRenderType")
    private static HandRenderType invokeGetUsingItemHandRenderType(ClientPlayerEntity player) {
        throw new AssertionError();
    }
    
    @Inject(method = "getHandRenderType", at = @At("HEAD"), cancellable = true)
    private static void getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HandRenderType> cir) {
        cir.setReturnValue(HeldItemRenderer.invokeGetUsingItemHandRenderType(player));
    }*/
}
