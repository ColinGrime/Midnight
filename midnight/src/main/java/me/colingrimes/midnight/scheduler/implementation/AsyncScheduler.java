package me.colingrimes.midnight.scheduler.implementation;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.task.AsyncTask;
import me.colingrimes.midnight.scheduler.task.Task;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public class AsyncScheduler implements Scheduler {

    @Nonnull
    @Override
    public <T> CompletableFuture<T> run(@Nonnull Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(MidnightPlugin.getInstance(), () -> {
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
    public <T> CompletableFuture<T> runLater(@Nonnull Callable<T> task, long delayTicks) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskLaterAsynchronously(MidnightPlugin.getInstance(), () -> {
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
    public Task runRepeating(@Nonnull Runnable runnable, long delayTicks, long periodTicks) {
        return new AsyncTask(runnable, delayTicks, periodTicks);
    }
}