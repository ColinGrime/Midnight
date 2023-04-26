package me.colingrimes.midnight.event.util;

import org.bukkit.event.inventory.InventoryAction;

import javax.annotation.Nonnull;

/**
 * An enumeration that represents various simplified inventory actions performed by a player.
 */
public enum SimpleAction {
    /**
     * Player picks up an item from a slot.
     * This includes picking up the entire stack, half of the stack, or a single item from the stack.
     */
    PICKUP,
    /**
     * Player places an item in a slot.
     * This includes placing the entire stack, a part of the stack, or a single item from the stack.
     */
    PLACE,
    /**
     * Player drops an item from the cursor.
     * This includes dropping the entire stack or a single item.
     */
    DROP,
    /**
     * Player swaps the item in the cursor with the item in the slot.
     */
    SWAP,
    /**
     * Player moves an item to another inventory (e.g., from the hotbar to the main inventory, or vice versa).
     */
    TRANSFER,
    /**
     * Player double-clicks to collect items of the same type in the inventory.
     */
    COLLECT,
    /**
     * Any other unknown or unsupported action.
     */
    UNKNOWN;

    /**
     * Gets the corresponding {@link SimpleAction} from an {@link InventoryAction}.
     * @param inventoryAction the inventory action
     * @return the corresponding simplified inventory action
     */
    @Nonnull
    public static SimpleAction fromInventoryAction(@Nonnull InventoryAction inventoryAction) {
        switch (inventoryAction) {
            case PICKUP_ALL:
            case PICKUP_HALF:
            case PICKUP_ONE:
            case PICKUP_SOME:
                return PICKUP;
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                return PLACE;
            case DROP_ALL_CURSOR:
            case DROP_ONE_CURSOR:
            case DROP_ALL_SLOT:
            case DROP_ONE_SLOT:
                return DROP;
            case SWAP_WITH_CURSOR:
                return SWAP;
            case MOVE_TO_OTHER_INVENTORY:
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP:
                return TRANSFER;
            case COLLECT_TO_CURSOR:
                return COLLECT;
            default:
                return UNKNOWN;
        }
    }
}
