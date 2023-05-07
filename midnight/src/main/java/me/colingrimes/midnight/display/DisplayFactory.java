package me.colingrimes.midnight.display;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.display.implementation.ActionBar;
import me.colingrimes.midnight.display.implementation.BossBar;
import me.colingrimes.midnight.display.implementation.Title;
import me.colingrimes.midnight.display.manager.DisplayType;

import javax.annotation.Nonnull;

public class DisplayFactory {

    private final Midnight plugin;

    public DisplayFactory(@Nonnull Midnight plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a {@link Display} based on the provided {@link DisplayType} and text.
     * @param displayType the type of display to create
     * @param text the text to display
     * @return the created display
     */
    @Nonnull
    public Display create(DisplayType displayType, @Nonnull String text) {
        return switch (displayType) {
            case BOSS_BAR -> createBossBar(text);
            case ACTION_BAR -> createActionBar(text);
            case TITLE -> createTitle(text);
        };
    }

    /**
     * Creates a {@link BossBar} with the provided text.
     * @param text the text to display
     * @return the created bossbar
     */
    @Nonnull
    public BossBar createBossBar(@Nonnull String text) {
        return new BossBar(plugin, text);
    }

    /**
     * Creates an {@link ActionBar} with the provided text.
     * @param text the text to display
     * @return the created actionbar
     */
    @Nonnull
    public ActionBar createActionBar(@Nonnull String text) {
        return new ActionBar(plugin, text);
    }

    /**
     * Creates a {@link Title} with the provided text.
     * @param text the text to display
     * @return the created title
     */
    @Nonnull
    public Title createTitle(@Nonnull String text) {
        return new Title(text);
    }
}
