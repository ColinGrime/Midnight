package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.type.DisplayType;
import me.colingrimes.midnight.event.DisplayHideEvent;
import me.colingrimes.midnight.event.DisplayShowEvent;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.Task;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.text.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionBar implements Display {

    private final Map<Player, Task> tasks = new HashMap<>();
    private String text;
    private boolean visible;

    public ActionBar(@Nonnull String text) {
        this.text = Text.color(text);
        this.visible = true;
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
        DisplayShowEvent displayShowEvent = new DisplayShowEvent(this, player);
        Common.call(displayShowEvent);
        if (!displayShowEvent.isCancelled()) {
            startTask(player);
        }
    }

    @Override
    public void hide(@Nonnull Player player) {
        Common.call(new DisplayHideEvent(this, player));
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
     *
     * @param player the player
     */
    private void startTask(@Nonnull Player player) {
        stopTask(player);

        // Set up the action bar task.
        Task task = Scheduler.SYNC.runRepeating(() -> {
            if (visible) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
            }
        }, 0L, 20L);

        tasks.put(player, task);
    }

    /**
     * Stops the task for the player.
     *
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
