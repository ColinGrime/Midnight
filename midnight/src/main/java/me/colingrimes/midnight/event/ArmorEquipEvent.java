package me.colingrimes.midnight.event;

import me.colingrimes.midnight.event.util.EquipAction;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An event that is called when a player equips, unequips, or swaps armor.
 */
public class ArmorEquipEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final EquipAction action;
    private final ItemStack newArmor;
    private final ItemStack oldArmor;
    private boolean cancelled;

    /**
     * Constructs a new ArmorEquipEvent.
     *
     * @param player   the player performing the action
     * @param action   the equip action being performed
     * @param oldArmor the old armor item being unequipped
     * @param newArmor the new armor item being equipped
     */
    public ArmorEquipEvent(@Nonnull Player player, @Nonnull EquipAction action, @Nullable ItemStack newArmor, @Nullable ItemStack oldArmor) {
        this.player = player;
        this.action = action;
        this.oldArmor = oldArmor;
        this.newArmor = newArmor;
        this.cancelled = false;
    }

    /**
     * Gets the player who performed the equip action.
     *
     * @return the player
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the equip action being performed.
     *
     * @return the equip action
     */
    @Nonnull
    public EquipAction getAction() {
        return action;
    }

    /**
     * Gets the old armor item being unequipped.
     *
     * @return the old armor item
     */
    @Nonnull
    public Optional<ItemStack> getOldArmor() {
        return Optional.ofNullable(oldArmor);
    }

    /**
     * Gets the new armor item being equipped.
     *
     * @return the new armor item
     */
    @Nonnull
    public Optional<ItemStack> getNewArmor() {
        return Optional.ofNullable(newArmor);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
