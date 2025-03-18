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
import javax.annotation.Nullable;
import java.util.List;

public class BossBar implements Display {

    private final org.bukkit.boss.BossBar bossBar;
    private String id;

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
    public List<Player> players() {
        return bossBar.getPlayers();
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

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
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
     * This moves the bar from 0.0 to 1.0 over the specified number of ticks.
     * <p>
     * The boss bar will be hidden for all players after the animation is complete.
     *
     * @param ticks the number of ticks to animate the boss bar
     */
    public void animateProgress(int ticks) {
        animateProgress(ticks, 0.0);
    }

    /**
     * Animates the progress of the boss bar.
     * This moves the bar from {@code startProgress} to 1.0 over the specified number of ticks.
     * Progress bounds is [0, 1.0].
     * <p>
     * The boss bar will be hidden for all players after the animation is complete.
     *
     * @param ticks the number of ticks to animate the boss bar
     * @param startProgress the starting progress of the boss bar
     */
    public void animateProgress(int ticks, double startProgress) {
        animateProgress(ticks, startProgress, 1.0);
    }

    /**
     * Animates the progress of the boss bar.
     * This moves the bar from {@code startProgress} to {@code endProgress} over the specified number of ticks.
     * Progress bounds is [0, 1.0].
     * <p>
     * If {@code startProgress} is higher than {@code endProgress}, the bar will animate backwards.
     * <p>
     * The boss bar will be hidden for all players after the animation is complete.
     *
     * @param ticks the number of ticks to animate the boss bar
     * @param startProgress the starting progress of the boss bar
     * @param endProgress the ending progress of the boss bar
     */
    public void animateProgress(int ticks, double startProgress, double endProgress) {
        animateProgress(ticks, startProgress, endProgress, true);
    }

    /**
     * Animates the progress of the boss bar.
     * This moves the bar from {@code startProgress} to {@code endProgress} over the specified number of ticks.
     * Progress bounds is [0, 1.0].
     * <p>
     * If {@code startProgress} is higher than {@code endProgress}, the bar will animate backwards.
     *
     * @param ticks the number of ticks to animate the boss bar
     * @param startProgress the starting progress of the boss bar
     * @param endProgress the ending progress of the boss bar
     * @param hideAfterAnimated true if the boss bar should be hidden for all players after the animation is complete
     */
    public void animateProgress(int ticks, double startProgress, double endProgress, boolean hideAfterAnimated) {
        double difference = endProgress - startProgress;
        double increment = difference / ticks;

        // The progress difference is too small, don't proceed.
        if (Math.abs(difference) < Math.abs(increment)) {
            bossBar.setVisible(false);
            return;
        }

        bossBar.setProgress(startProgress);
        Scheduler.sync().runRepeating((task) -> {
            double progress = bossBar.getProgress() + increment;
            if (Math.abs(progress - endProgress) > Math.abs(increment)) {
                bossBar.setProgress(progress);
            } else {
                bossBar.setProgress(endProgress);
                task.stop();
                if (hideAfterAnimated) {
                    Scheduler.sync().runLater(this::hideAll, 3L);
                }
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
