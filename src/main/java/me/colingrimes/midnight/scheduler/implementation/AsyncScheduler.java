package me.colingrimes.midnight.scheduler.implementation;

import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.AsyncTask;
import me.colingrimes.midnight.scheduler.task.SyncTask;
import me.colingrimes.midnight.scheduler.task.Task;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AsyncScheduler implements Scheduler {

    @Nonnull
    @Override
    public <T> CompletableFuture<T> call(@Nonnull Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(MidnightPlugin.get(), () -> {
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
        Bukkit.getScheduler().runTaskLaterAsynchronously(MidnightPlugin.get(), () -> {
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
    public Task runRepeating(@Nonnull Runnable runnable, long delayTicks, long periodTicks, long cancelTicks) {
        return new AsyncTask(runnable, delayTicks, periodTicks, cancelTicks);
    }

    @Nonnull
    @Override
    public Task runRepeating(@Nonnull Consumer<Task> task, long delay, long period, long cancelTicks) {
        return new SyncTask(task, delay, period, cancelTicks);
    }
}
