package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.manager.DisplayType;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class BossBar extends BaseDisplay {

    private final MidnightPlugin plugin;
    private final org.bukkit.boss.BossBar bossBar;

    public BossBar(@Nonnull MidnightPlugin plugin, @Nonnull String text) {
        this.plugin = plugin;
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
        Optional<Display> display = plugin.getDisplayManager().get(player, DisplayType.BOSS_BAR);
        if (display.isPresent() && getPriority() < display.get().getPriority()) {
            if (getPriority() < display.get().getPriority()) {
                return;
            }
        } else if (hasActiveBossBar(player)) {
            if (getPriority() < Display.DEFAULT_PRIORITY) {
                return;
            }
        }

        display.ifPresent(d -> d.hide(player));
        plugin.getDisplayManager().set(player, this);
        bossBar.addPlayer(player);
    }

    /**
     * Checks if the player has an externally active boss bar.
     * @param player the player to check
     * @return true if the player has an externally active boss bar
     */
    private boolean hasActiveBossBar(@Nonnull Player player) {
        Iterator<KeyedBossBar> bossBarIterator = Bukkit.getBossBars();
        while (bossBarIterator.hasNext()) {
            if (bossBarIterator.next().getPlayers().contains(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void hide(@Nonnull Player player) {
        Optional<Display> display = plugin.getDisplayManager().get(player, DisplayType.BOSS_BAR);
        if (display.isPresent() && display.get().equals(this)) {
            plugin.getDisplayManager().remove(player, DisplayType.BOSS_BAR);
        }

        bossBar.removePlayer(player);
    }

    @Override
    public boolean isVisible() {
        return bossBar.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        bossBar.setVisible(true);
    }

    /**
     * Sets the color of the boss bar.
     * @param color the new color of the boss bar
     */
    public void setColor(@Nonnull BarColor color) {
        bossBar.setColor(color);
    }

    /**
     * Gets the color of the boss bar.
     * @return the color of the boss bar
     */
    @Nonnull
    public BarColor getColor() {
        return bossBar.getColor();
    }

    /**
     * Sets the style of the boss bar.
     * @param style the new style of the boss bar
     */
    public void setStyle(@Nonnull BarStyle style) {
        bossBar.setStyle(style);
    }

    /**
     * Gets the style of the boss bar.
     * @return the style of the boss bar
     */
    @Nonnull
    public BarStyle getStyle() {
        return bossBar.getStyle();
    }

    /**
     * Sets the progress of the boss bar.
     * @param progress the new progress of the boss bar
     */
    public void setProgress(double progress) {
        bossBar.setProgress(progress);
    }

    /**
     * Gets the progress of the boss bar.
     * @return the progress of the boss bar
     */
    public double getProgress() {
        return bossBar.getProgress();
    }

    /**
     * Adds a flag to the boss bar.
     * @param flag the flag to add
     */
    public void addFlag(@Nonnull BarFlag flag) {
        bossBar.addFlag(flag);
    }

    /**
     * Removes a flag from the boss bar.
     * @param flag the flag to remove
     */
    public void removeFlag(@Nonnull BarFlag flag) {
        bossBar.removeFlag(flag);
    }

    /**
     * Checks if the boss bar has a flag.
     * @param flag the flag to check for
     * @return true if the boss bar has the flag
     */
    public boolean hasFlag(@Nonnull BarFlag flag) {
        return bossBar.hasFlag(flag);
    }
}
