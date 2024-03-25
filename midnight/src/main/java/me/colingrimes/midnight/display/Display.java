package me.colingrimes.midnight.display;

import me.colingrimes.midnight.display.implementation.ActionBar;
import me.colingrimes.midnight.display.implementation.BossBar;
import me.colingrimes.midnight.display.implementation.Title;
import me.colingrimes.midnight.display.type.DisplayType;
import me.colingrimes.midnight.scheduler.Scheduler;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Represents a display element in Minecraft, such as boss bars, action bars, and titles.
 * Display elements can show text or other information to the player.
 */
public interface Display {

    /**
     * Creates a {@code Display} based on the provided {@code DisplayType} and text.
     *
     * @param displayType the type of display to create
     * @param text        the text to display
     * @return the created display
     */
    @Nonnull
    static Display create(DisplayType displayType, @Nonnull String text) {
        return switch (displayType) {
            case BOSS_BAR -> bossBar(text);
            case ACTION_BAR -> actionBar(text);
            case TITLE -> title(text);
        };
    }

    /**
     * Creates a {@code BossBar} with the provided text.
     *
     * @param text the text to display
     * @return the created bossbar
     */
    @Nonnull
    static BossBar bossBar(@Nonnull String text) {
        return new BossBar(text);
    }

    /**
     * Creates an {@code ActionBar} with the provided text.
     *
     * @param text the text to display
     * @return the created actionbar
     */
    @Nonnull
    static ActionBar actionBar(@Nonnull String text) {
        return new ActionBar(text);
    }

    /**
     * Creates a {@code Title} with the provided text.
     *
     * @param text the text to display
     * @return the created title
     */
    @Nonnull
    static Title title(@Nonnull String text) {
        return new Title(text);
    }

    /**
     * Gets the priority of the display element.
     *
     * @return the priority of the display element
     */
    @Nonnull
    DisplayType getType();

    /**
     * Gets the text of the display element.
     *
     * @return the text of the display element
     */
    @Nonnull
    String getText();

    /**
     * Sets the text of the display element.
     *
     * @param text the new text for the display element
     */
    void setText(@Nonnull String text);

    /**
     * Gets the set of players currently watching the display element.
     *
     * @return a list of players watching the display element
     */
    @Nonnull
    List<Player> players();

    /**
     * Shows the display element to the specified player indefinitely.
     *
     * @param player the player to show the display element to
     */
    void show(@Nonnull Player player);

    /**
     * Shows the display element to the specified player for a certain amount of time in seconds.
     *
     * @param player the player to show the display element to
     * @param duration the duration in seconds the display element will be visible
     */
    default void show(@Nonnull Player player, int duration) {
        show(player, duration, TimeUnit.SECONDS);
    }

    /**
     * Shows the display element to the specified player for a certain amount of time.
     *
     * @param player the player to show the display element to
     * @param duration the duration the display element will be visible
     * @param unit the time unit of the duration
     */
    default void show(@Nonnull Player player, long duration, @Nonnull TimeUnit unit) {
        show(player);
        Scheduler.sync().runLater(() -> hide(player), unit.toSeconds(duration) * 20);
    }

    /**
     * Hides the display element from the specified player.
     *
     * @param player the player to hide the display element from
     */
    void hide(@Nonnull Player player);

    /**
     * Hides the display element from all players.
     */
    default void hideAll() {
        players().forEach(this::hide);
    }

    /**
     * Checks if the display element is currently visible to players.
     *
     * @return true if the display element is visible, false otherwise
     */
    boolean isVisible();

    /**
     * Sets whether the display element is visible to players.
     *
     * @param visible true if the display element should be visible
     */
    void setVisible(boolean visible);
}
