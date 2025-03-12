package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.type.DisplayType;
import me.colingrimes.midnight.event.DisplayHideEvent;
import me.colingrimes.midnight.event.DisplayShowEvent;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class BossBar implements Display {

    private final org.bukkit.boss.BossBar bossBar;

    public BossBar(@Nonnull String text) {
        this.bossBar = Bukkit.createBossBar(Text.color(text), BarColor.PURPLE, BarStyle.SOLID);
        this.bossBar.setVisible(true);
    }

    @Nonnull
    @Override
    public DisplayType getType() {
        return DisplayType.BOSS_BAR;
    }

    @Nonnull
    @Override
    public String getText() {
        return bossBar.getTitle();
    }

    @Override
    public void setText(@Nonnull String text) {
        bossBar.setTitle(Text.color(text));
    }

    @Nonnull
    @Override
    public List<Player> players() {
        return bossBar.getPlayers();
    }

    @Override
    public void show(@Nonnull Player player) {
        DisplayShowEvent displayShowEvent = new DisplayShowEvent(this, player);
        Common.call(displayShowEvent);
        if (!displayShowEvent.isCancelled()) {
            bossBar.addPlayer(player);
        }
    }

    @Override
    public void hide(@Nonnull Player player) {
        Common.call(new DisplayHideEvent(this, player));
        bossBar.removePlayer(player);
    }

    @Override
    public boolean isVisible() {
        return bossBar.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        bossBar.setVisible(visible);
    }

    /**
     * Gets the color of the boss bar.
     *
     * @return the color of the boss bar
     */
    @Nonnull
    public BarColor getColor() {
        return bossBar.getColor();
    }

    /**
     * Sets the color of the boss bar.
     *
     * @param color the new color of the boss bar
     */
    public void setColor(@Nonnull BarColor color) {
        bossBar.setColor(color);
    }

    /**
     * Gets the style of the boss bar.
     *
     * @return the style of the boss bar
     */
    @Nonnull
    public BarStyle getStyle() {
        return bossBar.getStyle();
    }

    /**
     * Sets the style of the boss bar.
     *
     * @param style the new style of the boss bar
     */
    public void setStyle(@Nonnull BarStyle style) {
        bossBar.setStyle(style);
    }

    /**
     * Gets the progress of the boss bar.
     *
     * @return the progress of the boss bar
     */
    public double getProgress() {
        return bossBar.getProgress();
    }

    /**
     * Sets the progress of the boss bar.
     *
     * @param progress the new progress of the boss bar
     */
    public void setProgress(double progress) {
        bossBar.setProgress(progress);
    }

    /**
     * Animates the progress of the boss bar.
     * This sets the progress to 0.0 (empty) to 1.0 (full) over the specified number of ticks.
     * The boss bar will become invisible after the animation is complete.
     *
     * @param ticks the number of ticks to animate the boss bar
     */
    public void animateProgress(int ticks) {
        animateProgress(ticks, true);
    }

    /**
     * Animates the progress of the boss bar.
     * This sets the progress to 0.0 (empty) to 1.0 (full) over the specified number of ticks.
     *
     * @param ticks the number of ticks to animate the boss bar
     * @param hideAfterAnimated true if the boss bar should disappear after the animation is complete
     */
    public void animateProgress(int ticks, boolean hideAfterAnimated) {
        bossBar.setProgress(0.0);
        Scheduler.sync().runRepeating((task) -> {
            double progress = bossBar.getProgress() + (1.0 / ticks);
            if (progress < 1.0) {
                bossBar.setProgress(progress);
            } else {
                bossBar.setProgress(1.0);
                Scheduler.sync().runLater(() -> setVisible(!hideAfterAnimated), 3L);
                task.stop();
            }
        }, 1L, 1L);
    }

    /**
     * Checks if the boss bar has a flag.
     *
     * @param flag the flag to check for
     * @return true if the boss bar has the flag
     */
    public boolean hasFlag(@Nonnull BarFlag flag) {
        return bossBar.hasFlag(flag);
    }

    /**
     * Adds a flag to the boss bar.
     *
     * @param flag the flag to add
     */
    public void addFlag(@Nonnull BarFlag flag) {
        bossBar.addFlag(flag);
    }

    /**
     * Removes a flag from the boss bar.
     *
     * @param flag the flag to remove
     */
    public void removeFlag(@Nonnull BarFlag flag) {
        bossBar.removeFlag(flag);
    }
}
