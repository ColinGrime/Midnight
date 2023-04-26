package me.colingrimes.midnight.event;

import me.colingrimes.midnight.event.util.SimpleAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * A custom event that simplifies the handling of {@link InventoryClickEvent}
 * and provides more descriptive method names.
 */
public class SimpleClickEvent extends InventoryClickEvent {

    /**
     * Constructs a new {@link SimpleClickEvent} from an {@link InventoryClickEvent}.
     * @param event the inventory click event
     */
    public SimpleClickEvent(InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction());
    }

    /**
     * Gets the player who performed the inventory click action.
     * @return the player
     */
    @Nonnull
    public Player getPlayer() {
        return (Player) getWhoClicked();
    }

    /**
     * Gets the simplified inventory action performed by the player.
     * @return the action
     */
    @Nonnull
    public SimpleAction getSimpleAction() {
        return SimpleAction.fromInventoryAction(getAction());
    }

    /**
     * Gets the item in the cursor.
     * @return the cursor item
     */
    @Nonnull
    public Optional<ItemStack> getCursorItem() {
        return Optional.ofNullable(getCursor());
    }

    /**
     * Gets the item that was clicked in the inventory action.
     * @return the clicked item
     */
    @Nonnull
    public Optional<ItemStack> getClickedItem() {
        return Optional.ofNullable(getCurrentItem());
    }
}
