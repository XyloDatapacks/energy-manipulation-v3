package com.xylo_datapacks.energy_manipulation.screen.spell_book;

import com.xylo_datapacks.energy_manipulation.config.SpellBookInfo;
import com.xylo_datapacks.energy_manipulation.item.SpellBookItem;
import com.xylo_datapacks.energy_manipulation.item.SpellBookPageItem;
import com.xylo_datapacks.energy_manipulation.item.spell_book.gui.GuiManager;
import com.xylo_datapacks.energy_manipulation.api.Dimension;
import com.xylo_datapacks.energy_manipulation.api.Point;
import com.xylo_datapacks.energy_manipulation.registry.MenuTypeRegistry;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Map;
import java.util.function.Consumer;

public class SpellBookScreenHandler extends ScreenHandler {

    public static final int BUTTON_CATEGORY_ID_OFFSET = 5000;
    public enum BUTTON_CATEGORY {
        NODE_SELECT_BUTTON(0),
        TOGGLE_NODE_EXPANDED_BUTTON(1),
        ADD_ELEMENT_TO_LIST_BUTTON(2),
        REMOVE_NODE_FROM_LIST_BUTTON(3);
        
        private final int id;
        
        BUTTON_CATEGORY(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public int getOffset() {
            return id * BUTTON_CATEGORY_ID_OFFSET;
        }
    }
    private final ItemStack spellBookStack;
    private final int padding = 8;
    private final int titleSpace = 10;
    private final int verticalOffset = 68;
    private final GuiManager guiManager;
    private BackpackInventory inventory;
    private Consumer<Boolean> NodeListUpdateConsumer;

    public SpellBookScreenHandler(int synchronizationID, PlayerInventory playerInventory, SpellBookItem.SpellBookMenuData spellBookMenuData) {
        this(synchronizationID, playerInventory, spellBookMenuData.spellBookItemStack());
    }

    public SpellBookScreenHandler(int synchronizationID, PlayerInventory playerInventory, ItemStack spellBookStack) {
        super(MenuTypeRegistry.SPELL_BOOK_MENU_TYPE, synchronizationID);
        this.spellBookStack = spellBookStack;
        this.guiManager = new GuiManager(null);
        
        if (spellBookStack.getItem() instanceof SpellBookItem) {
            setupContainer(playerInventory, spellBookStack);
        } else {
            PlayerEntity player = playerInventory.player;
            this.onClosed(player);
        }

        // server rpc
        this.addServerboundMessage(SelectorValue.class, this::updateSelectorValue);
    }

    private void setupContainer(PlayerInventory playerInventory, ItemStack spellBookStack) {
        Dimension dimension = getDimension();
        SpellBookInfo tier = getItem().getTier();
        int rowWidth = tier.getRowWidth();
        int numberOfRows = tier.getNumberOfRows();
        
        // create inventory
        inventory = new BackpackInventory(rowWidth * numberOfRows) {
            @Override
            public void markDirty() {
                SpellBookItem.setBackpackContent(spellBookStack, this, playerInventory.player.getRegistryManager());
                super.markDirty();
            }

            @Override
            public void onInventoryChanged(Inventory sender) {
                loadPageSpell();
            }
        };
        // update inventory from nbt
        loadInventoryFromNbt(playerInventory.player.getRegistryManager(), spellBookStack);

        // spell book inventory slots
        for (int i = 0; i < 8; i++) {
            int x = i % 2 + (i / 4)*2; // (i / 4)*2 add 2 if i >= 4 (0,1,0,1,2,3,2,3)
            int y = (i - (i / 4)*4) / 2; // -(i / 4)*4) removes 4 if i >= 4 (0,0,1,1,0,0,1,1)
            Point backpackSlotPosition = getBackpackSlotPosition(dimension, x, y);
            addSlot(new BackpackLockedSlot(inventory, i, backpackSlotPosition.x + 1, backpackSlotPosition.y + 1));
        }

        // player inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, x, y);
                this.addSlot(new BackpackLockedSlot(playerInventory, x + y * 9 + 9, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
            }
        }

        // hot bar
        for (int x = 0; x < 9; ++x) {
            Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, x, 3);
            this.addSlot(new BackpackLockedSlot(playerInventory, x, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
        }
    }

    public void updateSelectorValue(SelectorValue selectorValue) {
        System.out.println("UPDATING SELECTOR " + (this.player().getWorld().isClient ? "CLIENT" : "SERVER"));
        this.guiManager.setSelectedNodeValue(selectorValue.value());
        updatePageSpell();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id == -1) {
            guiManager.setPreviousNodeClass();
            updatePageSpell();
        }
        else if (id == -2) {
            guiManager.setNextNodeClass();
            updatePageSpell();
        }
        else if (id >= 0) {
            String path = guiManager.getPathAtIndex(id % BUTTON_CATEGORY_ID_OFFSET);
            if (path == null) return false;
            
            int categoryId = id / BUTTON_CATEGORY_ID_OFFSET;
            if (categoryId == BUTTON_CATEGORY.NODE_SELECT_BUTTON.getId()) {
                // update the selected node in the guiManager from the id
                guiManager.selectNode(path);
                sendNodeListUpdate();
            } else if (categoryId == BUTTON_CATEGORY.TOGGLE_NODE_EXPANDED_BUTTON.getId()) {
                // toggle expanded
                guiManager.getNodeAtPath(path).node().getGuiData().toggleExpanded();
                updatePageSpell();
            } else if (categoryId == BUTTON_CATEGORY.ADD_ELEMENT_TO_LIST_BUTTON.getId()) {
                // add element to list
                guiManager.addEntryToList(path);
                updatePageSpell();
            } else if (categoryId == BUTTON_CATEGORY.REMOVE_NODE_FROM_LIST_BUTTON.getId()) {
                // remove from list
                guiManager.removeNodeFromList(path);
                updatePageSpell();
            }
        }
        return true;
    }

    /**
     * has to be called after every manipulation from handledScreen
     */
    public void updatePageSpell() {
        if (this.player() != null) {
            System.out.println("SAVING SPELL " + (this.player().getWorld().isClient ? "CLIENT" : "SERVER"));
        }
        else {
            System.out.println("SAVING SPELL");
        }
        // get page itemStack
        ItemStack page = inventory.getStack(0);
        if (!page.isEmpty() && page.getItem() instanceof SpellBookPageItem) {
            SpellBookPageItem.setSpell(page, guiManager.toNbt());
            inventory.markDirty();
        }
    }

    /**
     * has to be called by constructor and after every inventory change
     */
    private void loadPageSpell() {
        if (this.player() != null) {
            System.out.println("LOADING SPELL " + (this.player().getWorld().isClient ? "CLIENT" : "SERVER"));
        } 
        else {
            System.out.println("LOADING SPELL");
        }
        // get page itemStack
        ItemStack page = inventory.getStack(0);
        if (!page.isEmpty() && page.getItem() instanceof SpellBookPageItem) {
            this.guiManager.setRootNode(SpellBookPageItem.getSpell(page));
        } 
        else {
            this.guiManager.reset();   
        }
        System.out.println("LOADED SPELL: " + guiManager.toNbt());
        // update screen
        sendNodeListUpdate();
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* Helper methods */
    
    public ItemStack getSpellBookStack() {
        return spellBookStack;
    }
    
    public SpellBookItem getItem() {
        return (SpellBookItem) spellBookStack.getItem();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return spellBookStack.getItem() instanceof SpellBookItem;
    }

    private void loadInventoryFromNbt(RegistryWrapper.WrapperLookup registries, ItemStack spellBookStack) {
        Map<Integer, ItemStack> stacks = SpellBookItem.getBackpackContents(registries, spellBookStack);
        
        inventory.clear();
        stacks.forEach((slot, element) -> {
            inventory.setStack(slot, element);            
        });
    }
    
    
    public GuiManager getGuiManager() { return guiManager; }
    
    /**
     * call in screen constructor
     */
    public void registerNodeListUpdate(Consumer<Boolean> NodeListUpdateConsumer) { 
        this.NodeListUpdateConsumer = NodeListUpdateConsumer; 
    }
    
    private void sendNodeListUpdate() {
        if (NodeListUpdateConsumer != null) {
            System.out.println("SENDING NODE LIST UPDATE");
            this.NodeListUpdateConsumer.accept(true);
        }
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* Slot positioning */
    
    public Dimension getDimension() {
        SpellBookInfo tier = getItem().getTier();
        return new Dimension(padding * 2 + Math.max(tier.getRowWidth(), 9) * 18, padding * 2 + titleSpace * 2 + 8 + (tier.getNumberOfRows() + 4) * 18);
    }

    public Point getBackpackSlotPosition(Dimension dimension, int x, int y) {
        SpellBookInfo tier = getItem().getTier();
        y++;
        return new Point(dimension.getWidth() / 2 - 9 * 9 + (x < 2 ? x - 3 : x + 8) * 18 , verticalOffset + dimension.getHeight() - padding - 4 * 18 - 3 + y * 18);
    }

    public Point getPlayerInvSlotPosition(Dimension dimension, int x, int y) {
        // SpellBookInfo tier = getItem().getTier();
        return new Point(dimension.getWidth() / 2 - 9 * 9 + x * 18, verticalOffset + dimension.getHeight() - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    /*----------------------------------------------------------------------------------------------------------------*/
    /* item moving */

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack toInsert = slot.getStack();
            itemStack = toInsert.copy();
            SpellBookInfo tier = getItem().getTier();
            if (index < tier.getNumberOfRows() * tier.getRowWidth()) {
                if (!this.insertItem(toInsert, tier.getNumberOfRows() * tier.getRowWidth(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(toInsert, 0, tier.getNumberOfRows() * tier.getRowWidth(), false)) {
                return ItemStack.EMPTY;
            }

            if (toInsert.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemStack;
    }
    
    /*----------------------------------------------------------------------------------------------------------------*/

    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Support classes */
    
    private class BackpackLockedSlot extends Slot {

        public BackpackLockedSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return stackMovementIsAllowed(getStack());
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            // If the "unstackables only" config option is turned on,
            // do not allow players to insert stacks with >1 max count.
            if (false) { //TODO: fix
                if (stack.getMaxCount() > 1) {
                    return false;
                }
            }

            // Do not allow players to insert shulkers into backpacks.
            if (true && inventory instanceof BackpackInventory) { //TODO: fix
                Item item = stack.getItem();
                if (item instanceof BlockItem blockItem) {
                    return !(blockItem.getBlock() instanceof ShulkerBoxBlock);
                }
            }

            return stackMovementIsAllowed(stack);
        }

        private boolean stackMovementIsAllowed(ItemStack stack) {
            return !(stack.getItem() instanceof SpellBookItem) && stack != spellBookStack;
        }
    }

    public static class BackpackInventory extends SimpleInventory implements InventoryChangedListener {

        public BackpackInventory(int slots) {
            super(slots);
            addListener(this);
        }

        @Override
        public void onInventoryChanged(Inventory sender) {
        }

        @Override
        public NbtList toNbtList(RegistryWrapper.WrapperLookup registries) {
            NbtList nbtList = new NbtList();
            for (int i = 0; i < this.size(); i++) {
                ItemStack itemStack = this.getStack(i);
                if (!itemStack.isEmpty()) {
                    NbtCompound itemStackCompound = (NbtCompound) itemStack.encode(registries);
                    itemStackCompound.putByte("Slot", (byte) i);
                    nbtList.add(itemStackCompound);
                }
            }
            
            return nbtList;
        }

        @Override
        public void readNbtList(NbtList list, RegistryWrapper.WrapperLookup registries) {
            this.clear();

            for (int i = 0; i < list.size(); i++) {
                NbtCompound itemStackCompound = list.getCompound(i);
                int slot = itemStackCompound.getInt("Slot");
                itemStackCompound.remove("Slot");
                ItemStack.fromNbt(registries, itemStackCompound).ifPresent(stack -> this.setStack(slot, stack));
            }
        }
    }
    
    public record SelectorValue(String value) {}

    
}