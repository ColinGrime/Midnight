package me.colingrimes.midnight.scheduler.task;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class SyncTask extends BaseTask {

	public SyncTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks) {
		super(runnable, Bukkit.getScheduler().runTaskTimer(MidnightPlugin.get(), runnable, delayTicks, periodTicks));
	}
}
