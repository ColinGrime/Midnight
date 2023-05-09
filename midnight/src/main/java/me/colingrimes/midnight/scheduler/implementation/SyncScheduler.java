package me.colingrimes.midnight.scheduler.implementation;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.SyncTask;
import me.colingrimes.midnight.scheduler.task.Task;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class SyncScheduler implements Scheduler {

    @Nonnull
    @Override
    public <T> CompletableFuture<T> call(@Nonnull Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(MidnightPlugin.get(), () -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Nonnull
    @Override
    public <T> CompletableFuture<T> callLater(@Nonnull Callable<T> task, long delayTicks) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskLater(MidnightPlugin.get(), () -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, delayTicks);
        return future;
    }

    @Nonnull
    @Override
    public Task runRepeating(@Nonnull Runnable task, long delay, long period) {
        return new SyncTask(task, delay, period);
    }
}
