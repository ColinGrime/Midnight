package me.colingrimes.midnight.scheduler.task;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;

public class SyncTask extends BaseTask {

	public SyncTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks) {
		super(runnable, delayTicks, periodTicks);
	}

	@Nonnull
	@Override
	public BukkitTask schedule(long delayTicks, long periodTicks) {
		return Bukkit.getScheduler().runTaskTimer(MidnightPlugin.get(), this, delayTicks, periodTicks);
	}
}
