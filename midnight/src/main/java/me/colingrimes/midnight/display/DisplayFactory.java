package me.colingrimes.midnight.display;

import me.colingrimes.midnight.display.implementation.ActionBar;
import me.colingrimes.midnight.display.implementation.BossBar;
import me.colingrimes.midnight.display.implementation.Title;
import me.colingrimes.midnight.display.type.DisplayType;

import javax.annotation.Nonnull;

public class DisplayFactory {

    /**
     * Creates a {@code Display} based on the provided {@code DisplayType} and text.
     * @param displayType the type of display to create
     * @param text the text to display
     * @return the created display
     */
    @Nonnull
    public static Display create(DisplayType displayType, @Nonnull String text) {
        return switch (displayType) {
            case BOSS_BAR -> createBossBar(text);
            case ACTION_BAR -> createActionBar(text);
            case TITLE -> createTitle(text);
        };
    }

    /**
     * Creates a {@code BossBar} with the provided text.
     * @param text the text to display
     * @return the created bossbar
     */
    @Nonnull
    public static BossBar createBossBar(@Nonnull String text) {
        return new BossBar(text);
    }

    /**
     * Creates an {@code ActionBar} with the provided text.
     * @param text the text to display
     * @return the created actionbar
     */
    @Nonnull
    public static ActionBar createActionBar(@Nonnull String text) {
        return new ActionBar(text);
    }

    /**
     * Creates a {@code Title} with the provided text.
     * @param text the text to display
     * @return the created title
     */
    @Nonnull
    public static Title createTitle(@Nonnull String text) {
        return new Title(text);
    }
}
