package me.colingrimes.midnight.event;

import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.type.DisplayType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

/**
 * Called when a {@code Display} is shown.
 */
public class DisplayShowEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Display display;
    private final Player player;
    private boolean cancelled;

    public DisplayShowEvent(@Nonnull Display display, @Nonnull Player player) {
        this.display = display;
        this.player = player;
    }

    @Nonnull
    public Display getDisplay() {
        return display;
    }

    @Nonnull
    public DisplayType getType() {
        return display.getType();
    }

    @Nonnull
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
