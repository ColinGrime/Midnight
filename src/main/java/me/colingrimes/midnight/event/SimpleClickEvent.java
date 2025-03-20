package me.colingrimes.midnight.event;

import me.colingrimes.midnight.event.util.SimpleAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * A custom event that simplifies the handling of {@link InventoryClickEvent}.
 */
public class SimpleClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final InventoryClickEvent event;
    private boolean cancelled;

    /**
     * Constructs a new {@link SimpleClickEvent} from an {@link InventoryClickEvent}.
     *
     * @param event the inventory click event
     */
    public SimpleClickEvent(InventoryClickEvent event) {
        this.event = event;
        this.cancelled = event.isCancelled();
    }

    /**
     * Gets the player who performed the inventory click action.
     *
     * @return the player
     */
    @Nonnull
    public Player getPlayer() {
        return (Player) event.getWhoClicked();
    }

    /**
     * Gets the primary inventory.
     *
     * @return the inventory
     */
    @Nonnull
    public Inventory getInventory() {
        return event.getInventory();
    }

    /**
     * Gets the clicked inventory.
     *
     * @return the inventory
     */
    @Nullable
    public Inventory getClickedInventory() {
        return event.getClickedInventory();
    }

    /**
     * Gets the inventory view that was clicked.
     *
     * @return the inventory view
     */
    @Nonnull
    public InventoryView getView() {
        return event.getView();
    }

    /**
     * Gets the type of slot that was clicked.
     *
     * @return the slot type
     */
    @Nonnull
    public InventoryType.SlotType getSlotType() {
        return event.getSlotType();
    }

    /**
     * Gets the click type that was performed.
     *
     * @return the click type
     */
    @Nonnull
    public ClickType getClick() {
        return event.getClick();
    }

    /**
     * Gets the simplified inventory action performed by the player.
     *
     * @return the action
     */
    @Nonnull
    public SimpleAction getAction() {
        return SimpleAction.fromInventoryAction(event.getAction());
    }

    /**
     * Gets the inventory action that was performed.
     *
     * @return the inventory action
     */
    @Nonnull
    public InventoryAction getSpecificAction() {
        return event.getAction();
    }

    /**
     * Gets the item in the cursor.
     *
     * @return the cursor item
     */
    @Nonnull
    public Optional<ItemStack> getCursor() {
        return Optional.ofNullable(event.getCursor());
    }

    /**
     * Gets the item that was clicked in the inventory action.
     *
     * @return the clicked item
     */
    @Nonnull
    public Optional<ItemStack> getClicked() {
        return Optional.ofNullable(event.getCurrentItem());
    }

    /**
     * Gets the slot that was clicked.
     *
     * @return the slot
     */
    public int getSlot() {
        return event.getSlot();
    }

    /**
     * Gets the raw slot that was clicked.
     *
     * @return the raw slot
     */
    public int getRawSlot() {
        return event.getRawSlot();
    }

    /**
     * Gets the hotbar button that was clicked.
     *
     * @return the hotbar button
     */
    public int getHotbarButton() {
        return event.getHotbarButton();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
        event.setCancelled(cancel);
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
