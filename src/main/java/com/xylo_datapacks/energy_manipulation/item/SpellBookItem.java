package com.xylo_datapacks.energy_manipulation.item;

import com.xylo_datapacks.energy_manipulation.config.SpellBookInfo;
import com.xylo_datapacks.energy_manipulation.registry.EntityRegistry;
import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.networking.ModPackets;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpellBookItem extends Item implements FabricItem {
    private static final String CHARGED_KEY = "Charged";
    private static final String CHARGE_KEY = "Charge";
    private boolean charged = false;
    private boolean loaded = false;
    private boolean completed = false;
    
    public SpellBookItem(Item.Settings settings) {
        super(settings);
    }


    /*----------------------------------------------------------------------------------------------------------------*/
    /* Casting Logic */

    public SpellNode getSpellNode(ItemStack itemStack) {
        // get inventory items
        Map<Integer, ItemStack> spellBookContent = getBackpackContents(itemStack);
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
        SpellNode spellNode = getSpellNode(itemStack);
        SpellEntity spell = EntityRegistry.SPELL.spawn((ServerWorld) world, user.getBlockPos(), SpawnReason.TRIGGERED);
        if (spell != null) {
            spell.setOwner(user);
            spell.setSpellNode(spellNode);
            spell.runSpell();
        }
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
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        float f = SpellBookItem.getPullProgress(i, stack);
        if (!SpellBookItem.isCharged(stack)) {
            SpellBookItem.setCharged(stack, true);
            SpellBookItem.setCharge(stack, f);
            SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundCategory, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }

    public static boolean isCharged(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.getBoolean(CHARGED_KEY);
    }

    public static void setCharged(ItemStack stack, boolean charged) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean(CHARGED_KEY, charged);
    }

    public static float getCharge(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound == null) {
            return 0.0f;
        }
        return nbtCompound.getFloat(CHARGE_KEY);
    }
    
    public static void setCharge(ItemStack stack, float charge) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putFloat(CHARGE_KEY, charge);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float) SpellBookItem.getPullTime(stack);
            if (f < 0.2f) {
                this.charged = false;
                this.loaded = false;
                this.completed = false;
            }
            if (f >= 0.2f && !this.charged) {
                this.charged = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
            if (f >= 0.5f && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
            if (f >= 0.75f && !this.completed) {
                this.completed = true;
                //world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
        }
    }
    
    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public int internalGetMaxUseTime(ItemStack stack) {
        return SpellBookItem.getPullTime(stack) + 3;
    }

    public static int getPullTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }

    private SoundEvent getQuickChargeSound(int stage) {
        switch (stage) {
            case 1: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            }
            case 2: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            }
            case 3: {
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            }
        }
        return SoundEvents.ITEM_CROSSBOW_LOADING_START;
    }

    private static float getPullProgress(int useTicks, ItemStack stack) {
        float f = (float)useTicks / (float) SpellBookItem.getPullTime(stack);
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
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
        ContainerComponent containerComponent = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        return containerComponent.streamNonEmpty().count() > 0;
    }

    public static Map<Integer, ItemStack> getBackpackContents(ItemStack stack) {
        Map<Integer, ItemStack> stacks = new LinkedHashMap<>();
        NbtList tag = stack.getOrCreateNbt().getList("Inventory", NbtElement.COMPOUND_TYPE);

        for (NbtElement element : tag) {
            NbtCompound stackTag = (NbtCompound) element;
            ItemStack backpackStack = ItemStack.fromNbt(stackTag);
            stacks.put(stackTag.getInt("Slot"), backpackStack);
        }

        return stacks;
    }

    public static NbtCompound getSlotNbt(ItemStack stack, int slot) {
        ContainerComponent containerComponent = stack.getOrDefault(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
        
        
        NbtList tag = stack.getOrCreateNbt().getList("Inventory", NbtElement.COMPOUND_TYPE);

        // return the element with matching slot
        for (NbtElement element : tag) {
            NbtCompound stackTag = (NbtCompound) element;
            if (stackTag.getInt("Slot") == slot) {
                return stackTag;
            }
        }

        return null;
    }

    public static void wipeBackpack(ItemStack stack) {
        stack.getOrCreateNbt().remove("Inventory");
    }

    /*----------------------------------------------------------------------------------------------------------------*/

}
