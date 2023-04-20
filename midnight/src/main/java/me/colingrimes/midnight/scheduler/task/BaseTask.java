package me.colingrimes.midnight.scheduler.task;

import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;

abstract class BaseTask implements Task {

    private final AtomicInteger timesRan = new AtomicInteger(0);
    private final Runnable runnable;
    private final BukkitTask task;

    BaseTask(@Nonnull Runnable runnable, @Nonnull BukkitTask task) {
        this.runnable = runnable;
        this.task = task;
    }

    @Override
    public void run() {
        if (!isCancelled()) {
            runnable.run();
            timesRan.incrementAndGet();
        }
    }

    @Override
    public void stop() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public int getTimesRan() {
        return timesRan.get();
    }

    @Override
    public int getTaskId() {
        return task.getTaskId();
    }

    @Nonnull
    @Override
    public BukkitTask getBukkitTask() {
        return task;
    }
}