package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.manager.DisplayType;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.Task;
import me.colingrimes.midnight.util.player.Players;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ActionBar extends BaseDisplay {

    private final Map<Player, Task> tasks = new HashMap<>();
    private final MidnightPlugin plugin;
    private String text;
    private boolean visible;

    public ActionBar(@Nonnull MidnightPlugin plugin, @Nonnull String text) {
        this.plugin = plugin;
        this.text = Text.color(text);
    }

    @Nonnull
    @Override
    public DisplayType getType() {
        return DisplayType.ACTION_BAR;
    }

    @Nonnull
    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(@Nonnull String text) {
        this.text = Text.color(text);
    }

    @Nonnull
    @Override
    public List<Player> players() {
        return tasks.keySet().stream().toList();
    }

    @Override
    public void show(@Nonnull Player player) {
        Optional<Display> display = plugin.getDisplayManager().get(player, DisplayType.ACTION_BAR);
        if (display.isPresent() && getPriority() < display.get().getPriority()) {
            return;
        }

        plugin.getDisplayManager().set(player, this);
        startTask(player);
    }

    @Override
    public void show(@Nonnull Player player, long duration, @Nonnull TimeUnit unit) {
        show(player);
        Scheduler.SYNC.runLater(() -> hide(player), unit.toMillis(duration));
    }

    @Override
    public void hide(@Nonnull Player player) {
        Optional<Display> display = plugin.getDisplayManager().get(player, DisplayType.ACTION_BAR);
        if (display.isPresent() && display.get().equals(this)) {
            plugin.getDisplayManager().remove(player, DisplayType.ACTION_BAR);
        }

        stopTask(player);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Starts the task for the player.
     * @param player the player
     */
    private void startTask(@Nonnull Player player) {
        stopTask(player);

        // Set up the action bar task.
        Task task = Scheduler.SYNC.runRepeating(() -> {
            if (visible) {
                Players.sendActionBar(player, text);
            }
        }, 0L, 20L);

        tasks.put(player, task);
    }

    /**
     * Stops the task for the player.
     * @param player the player
     */
    private void stopTask(@Nonnull Player player) {
        Task task = tasks.get(player);
        if (task != null) {
            task.stop();
            tasks.remove(player);
        }
    }
}
