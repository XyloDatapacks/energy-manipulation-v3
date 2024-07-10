package com.xylo_datapacks.energy_manipulation.screen.spell_book;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.api.Counter;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.gui.GuiManager;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithList;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.AbstractNodeWithValue;
import com.xylo_datapacks.energy_manipulation.item.custom.spell_book.node.base_class.record.NodeResult;
import com.xylo_datapacks.energy_manipulation.api.Dimension;
import com.xylo_datapacks.energy_manipulation.screen.custom_owo.CollapsibleContainerV2;
import com.xylo_datapacks.energy_manipulation.screen.custom_owo.XyloOwoContainers;
import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.DraggableContainer;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(value= EnvType.CLIENT)
public class SpellBookHandledScreen extends BaseUIModelHandledScreen<FlowLayout, SpellBookScreenHandler> {
    private FlowLayout rootComponent;
    private Map<String, Boolean> expandedMap = new HashMap<>();
    
    
    public SpellBookHandledScreen(SpellBookScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.asset(Identifier.of(EnergyManipulation.MOD_ID, "spell_book/spell_book_menu")));
        
        Dimension dimension = handler.getDimension();
        this.backgroundWidth = dimension.getWidth();
        this.backgroundHeight = dimension.getHeight();
        //this.titleY = 7;
        //this.playerInventoryTitleX = handler.getPlayerInvSlotPosition(dimension, 0, 0).x;
        //this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    /**
     * Build the component hierarchy of this screen,
     * called after the adapter and root component have been
     * initialized by {@link #createAdapter()}
     *
     * @param rootComponent The root component created
     *                      in the previous initialization step
     */
    @Override
    protected void build(FlowLayout rootComponent) {
        this.rootComponent = rootComponent;
        
        refreshNodesList();
        this.handler.registerNodeListUpdate(bUpdate -> {
            refreshNodesList();
        });
    }
    
    public void refreshNodesList() {
        ScrollContainer<?> scrollContainer = rootComponent.childById(ScrollContainer.class, "nodes_list_scroll");
        if (scrollContainer == null) return;
        
        FlowLayout flowLayout = scrollContainer.childById(FlowLayout.class, "nodes_list_scroll_content");
        if (flowLayout == null) return;

        // needed to calculate the scroll percentage
        double flowLayoutHeightPreUpdate = flowLayout.fullSize().height();
        
        // reset and refresh list
        flowLayout.clearChildren();
        Map<String, NodeResult> nodeResults = this.handler.getGuiManager().getRootSubNodes();
        addButtons(flowLayout, nodeResults, new Counter());
        // refresh node info panel
        refreshNodeInfo();

        // calculate scroll percentage
        double maxScrollHeight = flowLayoutHeightPreUpdate - scrollContainer.height(); // container height minus the exposed area
        double newMaxScrollHeight = flowLayout.fullSize().height() - scrollContainer.height(); // new container height minus the exposed area
        double maxLayoutY = scrollContainer.y() - maxScrollHeight; // Y pos of the layout when scrolled down at 100%
        double distance = Math.abs(flowLayout.y() - maxLayoutY); // goes from maxScrollHeight at 0% to 0 at 100%
        double progressPercent = newMaxScrollHeight > 0.0 ? Math.max((Math.min((maxScrollHeight - distance) / newMaxScrollHeight, 1.0)), 0.0) : 0.0; // clamp [(maxScrollHeight - distance) / newMaxScrollHeight]
        // restore scroll percentage
        scrollContainer.scrollTo(progressPercent);
    }

    private void addButtons(FlowLayout flowLayout, Map<String, NodeResult> nodeResults, Counter nodeIndexCounter) {
        for (Map.Entry<String, NodeResult> entry : nodeResults.entrySet()) {
            // add button
            if (flowLayout != null) {
                flowLayout.child(generateNodeElement(entry.getKey(), entry.getValue(), nodeIndexCounter));
            }
        }
    }

    private Component generateNodeElement(String nodePath, NodeResult nodeResult, Counter nodeIndexCounter) {
        // display data for button
        GuiManager.ButtonDisplay buttonDisplay = GuiManager.getButtonDisplay(nodeResult);
        int nodeIndex = nodeIndexCounter.getValue();
        
        // create layout to contain button
        CollapsibleContainerV2 collapsibleTile = (CollapsibleContainerV2) XyloOwoContainers.collapsibleV2(Sizing.content(0), Sizing.content(0), Text.of(buttonDisplay.subNodeName()), nodeResult.node().getGuiData().getExpanded())
                .surface(Surface.DARK_PANEL)
                .id(nodePath);
        collapsibleTile.onToggled().subscribe(expanded -> {
            // node expanded packet
            if (((SpellBookScreenHandler) this.handler).onButtonClick(this.client.player, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.TOGGLE_NODE_EXPANDED_BUTTON.getOffset())) {
                this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.TOGGLE_NODE_EXPANDED_BUTTON.getOffset());
            }
        });
        
        // create button
        ButtonComponent buttonComponent = (ButtonComponent) Components.button(
                Text.literal(buttonDisplay.subNodeName() + ": " + buttonDisplay.nodeName()), button -> {
                    addNodeInfoPanel();
                    
                    // node selection packet
                    if (((SpellBookScreenHandler) this.handler).onButtonClick(this.client.player, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.NODE_SELECT_BUTTON.getOffset())) {
                        this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.NODE_SELECT_BUTTON.getOffset());
                    }
                })
                .id(nodePath)
                .horizontalSizing(Sizing.content(0))
                .verticalSizing(Sizing.fixed(16));
        
        // add button component to layout
        collapsibleTile.titleLayout().child(buttonComponent);
        
        // add "+" button for list nodes
        if (nodeResult.node() instanceof AbstractNodeWithList<?>) {
            ButtonComponent plusButton = (ButtonComponent) Components.button(
                            Text.literal("+"), button -> {
                                if (((SpellBookScreenHandler) this.handler).onButtonClick(this.client.player, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.ADD_ELEMENT_TO_LIST_BUTTON.getOffset())) {
                                    this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.ADD_ELEMENT_TO_LIST_BUTTON.getOffset());
                                }
                            })
                    .id(nodePath)
                    .horizontalSizing(Sizing.content(0))
                    .verticalSizing(Sizing.fixed(16));

            collapsibleTile.titleLayout().child(plusButton);
        }

        // add "remove" button for children of list nodes
        if (nodeResult.node().getParentNode() instanceof AbstractNodeWithList<?>) {
            ButtonComponent removeButton = (ButtonComponent) Components.button(
                            Text.literal("-"), button -> {
                                if (((SpellBookScreenHandler) this.handler).onButtonClick(this.client.player, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.REMOVE_NODE_FROM_LIST_BUTTON.getOffset())) {
                                    this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, nodeIndex + SpellBookScreenHandler.BUTTON_CATEGORY.REMOVE_NODE_FROM_LIST_BUTTON.getOffset());
                                }
                            })
                    .id(nodePath)
                    .horizontalSizing(Sizing.content(0))
                    .verticalSizing(Sizing.fixed(16));

            collapsibleTile.titleLayout().child(removeButton);
        }
        
        // add children recursive
        Map<String, NodeResult> nodeResults = this.handler.getGuiManager().getSubNodes(nodeResult);
        addButtons(collapsibleTile, nodeResults, nodeIndexCounter.increment());
        
        return collapsibleTile;
    }

    private void addNodeInfoPanel() {
        DraggableContainer<?> pop_up = rootComponent.childById(DraggableContainer.class, "node_info_pop_up");
        if (pop_up == null) {
            StackLayout screenStack = rootComponent.childById(StackLayout.class, "screen_stack");
            if (screenStack != null) {
                DraggableContainer<?> nodeInfoPopUp = model.expandTemplate(DraggableContainer.class, "node_info_pop_up", Map.of());
                nodeInfoPopUp.childById(ButtonComponent.class, "close_node_info_panel").onPress(buttonComponent -> {
                    screenStack.removeChild(nodeInfoPopUp);
                });
                screenStack.child(nodeInfoPopUp);
            }
        }
    }

    public void refreshNodeInfo() {
        DraggableContainer<?> pop_up = rootComponent.childById(DraggableContainer.class, "node_info_pop_up");
        if (pop_up == null) return;
        
        FlowLayout flowLayout = rootComponent.childById(FlowLayout.class, "node_info_scroll_content");
        if (flowLayout == null) return;
        
        NodeResult nodeResult = this.handler.getGuiManager().getSelectedNode();
        // reset children if needed
        if (nodeResult == null ||  nodeResult.node() == null) {
            flowLayout.clearChildren();
            return;
        };
        
        // add child (second page)
        if (flowLayout.children().isEmpty()) {
            final var second_page = this.model.expandTemplate(FlowLayout.class, "second_page", Map.of());
            flowLayout.child(second_page);
        }
        FlowLayout second_page = flowLayout.childById(FlowLayout.class, "second_page_content");

        /*------------------------------------------------------------------------------------------------------------*/
        /* Info and class info */
        
        // display data
        GuiManager.EditorInfo editorHeader = GuiManager.getEditorHeader(nodeResult);
        GuiManager.EditorInfo editorCurrentSelection = GuiManager.getEditorCurrentSelection(nodeResult);

        // update Header
        second_page.childById(LabelComponent.class, "node_info_name_label").text(Text.of(editorHeader.name()));
        second_page.childById(LabelComponent.class, "node_info_description_label").text(Text.of(editorHeader.description()));
        
        // update current selection
        second_page.childById(LabelComponent.class, "node_info_class_name_label").text(Text.of(editorCurrentSelection.name()));
        second_page.childById(LabelComponent.class, "node_info_class_description_label").text(Text.of(editorCurrentSelection.description()));
        
        
        // set prev button functionality
        ButtonComponent buttonPrev = second_page.childById(ButtonComponent.class, "node_info_class_button_prev");
        if (buttonPrev != null) {
            buttonPrev.onPress(buttonComponent -> {
                if (((SpellBookScreenHandler)this.handler).onButtonClick(this.client.player, -1)) {
                    this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, -1);
                }
            });
        }

        // set next button functionality
        ButtonComponent buttonNext = second_page.childById(ButtonComponent.class, "node_info_class_button_next");
        if (buttonNext != null) {
            buttonNext.onPress(buttonComponent -> {
                if (((SpellBookScreenHandler)this.handler).onButtonClick(this.client.player, -2)) {
                    this.client.interactionManager.clickButton(((SpellBookScreenHandler) this.handler).syncId, -2);
                }
            });
        }
        
        /*------------------------------------------------------------------------------------------------------------*/
        

        /*------------------------------------------------------------------------------------------------------------*/
        /* Selector */
        
        // get selector box layout (template which will contain the slector)
        FlowLayout selectorBoxLayout = second_page.childById(FlowLayout.class, "selector_box_layout");
        // if node is a node with value (so it needs a selector), add the selector to the layout 
        // (after removing eventual prev selector)
        if (nodeResult.node() instanceof AbstractNodeWithValue<?> nodeWithValue) {
            // if the layout is null, add it to the second page
            if (selectorBoxLayout == null) {
                selectorBoxLayout = this.model.expandTemplate(FlowLayout.class, "selector_box", Map.of());
                second_page.child(selectorBoxLayout);
            }
            // clear layout children
            selectorBoxLayout.clearChildren();
            // add selector (from node)
            selectorBoxLayout.child(nodeWithValue.getValueSelectorComponent(this.model, result -> {
                this.handler.sendMessage(new SpellBookScreenHandler.SelectorValue(String.valueOf(result)));
            }));
        }
        // if selector not needed and layout is already there, remove layout
        else {
            if (selectorBoxLayout != null) {
                second_page.removeChild(selectorBoxLayout);
            }
        }

        /*------------------------------------------------------------------------------------------------------------*/
        
        
    }
    
    
    
}
