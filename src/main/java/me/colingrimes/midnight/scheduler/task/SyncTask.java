package me.colingrimes.midnight.scheduler.task;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SyncTask extends BaseTask {

	public SyncTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks, long cancelTicks) {
		super(runnable, delayTicks, periodTicks, cancelTicks);
	}

	public SyncTask(@Nonnull Consumer<Task> consumer, long delayTicks, long periodTicks, long cancelTicks) {
		super(consumer, delayTicks, periodTicks, cancelTicks);
	}

	@Nonnull
	@Override
	public BukkitTask schedule(long delayTicks, long periodTicks) {
		return Bukkit.getScheduler().runTaskTimer(MidnightPlugin.get(), this, delayTicks, periodTicks);
	}
}
