package me.colingrimes.midnight.scheduler;

import me.colingrimes.midnight.scheduler.implementation.AsyncScheduler;
import me.colingrimes.midnight.scheduler.implementation.SyncScheduler;
import me.colingrimes.midnight.scheduler.task.Task;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a scheduler for running tasks.
 */
public interface Scheduler {

	Scheduler SYNC = new SyncScheduler();
	Scheduler ASYNC = new AsyncScheduler();

	/**
	 * Runs a task.
	 * @param task the task to run
	 * @return a future that will be completed when the task is finished
	 * @param <T> the type of the result
	 */
	@Nonnull
	<T> CompletableFuture<T> run(@Nonnull Callable<T> task);

	/**
	 * Runs a task.
	 * @param task the task to run
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> run(@Nonnull Runnable task) {
		return run(() -> {
			task.run();
			return null;
		});
	}

	/**
	 * Runs a task after a delay.
	 * @param task the task to run
	 * @param delayTicks the delay in ticks
	 * @return a future that will be completed when the task is finished
	 * @param <T> the type of the result
	 */
	@Nonnull
	<T> CompletableFuture<T> runLater(@Nonnull Callable<T> task, long delayTicks);

	/**
	 * Runs a task after a delay.
	 * @param task the task to run
	 * @param delayTicks the delay in ticks
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> runLater(@Nonnull Runnable task, long delayTicks) {
		return runLater(() -> {
			task.run();
			return null;
		}, delayTicks);
	}

	/**
	 * Runs a task repeatedly.
	 * @param task the task to run
	 * @param delayTicks the delay in ticks
	 * @param periodTicks the period in ticks
	 * @return a task that can be used to cancel the task
	 */
	@Nonnull
	Task runRepeating(@Nonnull Runnable task, long delayTicks, long periodTicks);
}