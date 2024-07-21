package com.xylo_datapacks.energy_manipulation.api;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

/** total rip-off of the GiveCommand class, but reworked to be used in code */
public class Give {
    
    public static void execute(ItemStack itemStack, ServerPlayerEntity serverPlayerEntity, int count) {
        int i = itemStack.getMaxCount();
        int j = i * 100;
        if (count > j) {
            return;
        } 
        else {
            int k = count;
            while (k > 0) {
                int l = Math.min(i, k);
                k -= l;
                ItemStack itemStack2 = itemStack.copyWithCount(l);
                boolean bl = serverPlayerEntity.getInventory().insertStack(itemStack2);
                if (bl && itemStack2.isEmpty()) {
                    ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack, false);
                    if (itemEntity != null) {
                        itemEntity.setDespawnImmediately();
                    }

                    serverPlayerEntity.getWorld()
                            .playSound(
                                    null,
                                    serverPlayerEntity.getX(),
                                    serverPlayerEntity.getY(),
                                    serverPlayerEntity.getZ(),
                                    SoundEvents.ENTITY_ITEM_PICKUP,
                                    SoundCategory.PLAYERS,
                                    0.2F,
                                    ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                            );
                    serverPlayerEntity.currentScreenHandler.sendContentUpdates();
                } else {
                    ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack2, false);
                    if (itemEntity != null) {
                        itemEntity.resetPickupDelay();
                        itemEntity.setOwner(serverPlayerEntity.getUuid());
                    }
                }
            }
        }
    }
}
