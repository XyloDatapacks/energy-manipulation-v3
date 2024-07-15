package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.config.SpellBookInfo;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.registry.ModEntityRegistry;
import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SpellBookItem extends Item implements FabricItem {
    private static final String CHARGED_KEY = "Charged";
    private static final String CHARGE_KEY = "Charge";
    private boolean charged = false;
    private boolean loaded = false;
    private boolean completed = false;
    private static final CrossbowItem.LoadingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.LoadingSounds(
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
    );
    
    public SpellBookItem(Item.Settings settings) {
        super(settings);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* Casting Logic */

    public SpellNode getSpellNode(PlayerEntity user, ItemStack itemStack) {
        // get inventory items
        Map<Integer, ItemStack> spellBookContent = getBackpackContents(user.getRegistryManager(), itemStack);
        // check if there is slot zero
        if (spellBookContent.containsKey(0)) {
            // check if slot zero is spell book page
            if (spellBookContent.get(0).getItem() instanceof SpellBookPageItem) {
                // fetch spell node
                GenericNode node = SpellBookPageItem.getSpell(spellBookContent.get(0));
                if (node instanceof SpellNode spellNode) {
                    return spellNode;
                }
            }
        }
        return null;
    }
    
    public void runSpell(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
        SpellNode spellNode = getSpellNode(user, itemStack);
        ProjectileShapeEntity spell = new ProjectileShapeEntity(user, world, new ItemStack(Items.ARROW), itemStack);
        spell.setOwner(user);
        spell.setSpellNode(spellNode);
        spell.runSpell();
        spell.setDisplayedItemStack(new ItemStack(Items.LECTERN));

        spell.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1, 0.0F);
        
        world.spawnEntity(spell);
    }


    /*----------------------------------------------------------------------------------------------------------------*/

    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* Usage Logic */
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (SpellBookItem.isCharged(itemStack)) {
            SpellBookItem.setCharged(itemStack, false);
            SpellBookItem.setCharge(itemStack, 0.f);
            if (!world.isClient) {
                runSpell(world, user, hand, itemStack);
            }
            return TypedActionResult.success(itemStack);
        }
        if (!SpellBookItem.isCharged(itemStack)) {
            this.charged = false;
            this.loaded = false;
            this.completed = false;
            user.setCurrentHand(hand);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float f = SpellBookItem.getPullProgress(i, stack, user);
        if (!SpellBookItem.isCharged(stack)) {
            SpellBookItem.setCharged(stack, true);
            SpellBookItem.setCharge(stack, f);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }

    public static boolean isCharged(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getBoolean(CHARGED_KEY);
    }

    public static void setCharged(ItemStack stack, boolean charged) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putBoolean(CHARGED_KEY, charged);
        }));
    }

    public static float getCharge(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getFloat(CHARGE_KEY);
    }
    
    public static void setCharge(ItemStack stack, float charge) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putFloat(CHARGE_KEY, charge);
        }));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            CrossbowItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            float f = (float)(stack.getMaxUseTime(user) - remainingUseTicks) / (float)getPullTime(stack, user);
            if (f < 0.2f) {
                this.charged = false;
                this.loaded = false;
                this.completed = false;
            }
            if (f >= 0.2f && !this.charged) {
                this.charged = true;
                loadingSounds.start().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }
            if (f >= 0.5f && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, 0.5F, 1.0F));
            }
            if (f >= 0.75f && !this.completed) {
                this.completed = true;
            }
        }
    }
    
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    public int internalGetMaxUseTime(ItemStack stack, LivingEntity user) {
        return SpellBookItem.getPullTime(stack, user) + 3;
    }

    public static int getPullTime(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.getCrossbowChargeTime(stack, user, 1.25F);
        return MathHelper.floor(f * 20.0F);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    CrossbowItem.LoadingSounds getLoadingSounds(ItemStack stack) {
        return (CrossbowItem.LoadingSounds)EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_LOADING_SOUNDS);
    }

    private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float)useTicks / (float)getPullTime(stack, user);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return SpellBookItem.getCharge(oldStack) == SpellBookItem.getCharge(new ItemStack(this));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* Gui */

    public static void openScreen(PlayerEntity player, ItemStack spellBookItemStack) {
        if (player.getWorld() != null && !player.getWorld().isClient()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory<SpellBookMenuData>() {
                @Override
                public SpellBookMenuData getScreenOpeningData(ServerPlayerEntity player) {
                    return new SpellBookMenuData(spellBookItemStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable(spellBookItemStack.getItem().getTranslationKey());
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new SpellBookScreenHandler(syncId, inv, spellBookItemStack);
                }
            });
        }
    }

    public record SpellBookMenuData(ItemStack spellBookItemStack) implements CustomPayload {
        public static final CustomPayload.Id<SpellBookMenuData> ID = new CustomPayload.Id<>(ModPackets.INIT_SPELL_BOOK_MENU_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, SpellBookMenuData> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, SpellBookMenuData::spellBookItemStack, SpellBookMenuData::new);
        
        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* Inventory */
    
    public SpellBookInfo getTier() {
        return new SpellBookInfo("spell book", 9, 1, false, "");
    }
    
    public static boolean isBackpackEmpty(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList inventoryNbt = nbtCompound.getList("Inventory", NbtElement.COMPOUND_TYPE);
        return inventoryNbt.isEmpty();
    }

    public static Map<Integer, ItemStack> getBackpackContents(RegistryWrapper.WrapperLookup registries, ItemStack stack) {
        Map<Integer, ItemStack> stacks = new LinkedHashMap<>();
        NbtCompound nbtCompound = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList inventoryNbt = nbtCompound.getList("Inventory", NbtElement.COMPOUND_TYPE);
        
        for (NbtElement element : inventoryNbt) {
            NbtCompound stackTag = (NbtCompound) element;
            ItemStack.fromNbt(registries, stackTag).ifPresent(backpackStack -> stacks.put(stackTag.getInt("Slot"), backpackStack));
        }
        
        return stacks;
    }
    
    public static void setBackpackContent(ItemStack stack, SimpleInventory inventory, RegistryWrapper.WrapperLookup registries)  {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.put("Inventory", inventory.toNbtList(registries));
        }));
    }

    public static void wipeBackpack(ItemStack stack) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.remove("Inventory");
        }));
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
