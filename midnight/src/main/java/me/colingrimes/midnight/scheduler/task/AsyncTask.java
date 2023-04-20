package me.colingrimes.midnight.scheduler.task;

import me.colingrimes.midnight.MidnightPlugin;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;

public class AsyncTask extends BaseTask {

	public AsyncTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks) {
		super(runnable, Bukkit.getScheduler().runTaskTimerAsynchronously(MidnightPlugin.getInstance(), runnable, delayTicks, periodTicks));
	}
}
