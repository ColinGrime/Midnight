package me.colingrimes.midnight.scheduler;

import me.colingrimes.midnight.scheduler.functional.Execution;
import me.colingrimes.midnight.scheduler.implementation.AsyncScheduler;
import me.colingrimes.midnight.scheduler.implementation.SyncScheduler;
import me.colingrimes.midnight.scheduler.task.Task;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Represents a scheduler for running tasks.
 */
public interface Scheduler {

	class Holder {
		private static final Scheduler SYNC = new SyncScheduler();
		private static final Scheduler ASYNC = new AsyncScheduler();
	}

	/**
	 * Gets the sync scheduler.
	 *
	 * @return the sync scheduler
	 */
	@Nonnull
	static Scheduler sync() {
		return Holder.SYNC;
	}

	/**
	 * Gets the async scheduler.
	 *
	 * @return the async scheduler
	 */
	@Nonnull
	static Scheduler async() {
		return Holder.ASYNC;
	}

	/**
	 * Calls the given callable.
	 *
	 * @param task the task to call
	 * @param <T>  the type of the result
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	<T> CompletableFuture<T> call(@Nonnull Callable<T> task);

	/**
	 * Executes the given execution.
	 *
	 * @param task the task to execute
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> execute(@Nonnull Execution task) {
		return call(() -> {
			task.execute();
			return null;
		});
	}

	/**
	 * Runs the given runnable.
	 *
	 * @param task the task to run
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> run(@Nonnull Runnable task) {
		return call(() -> {
			task.run();
			return null;
		});
	}

	/**
	 * Calls the given callable after a delay.
	 *
	 * @param task       the task to call
	 * @param delayTicks the delay in ticks
	 * @param <T>        the type of the result
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	<T> CompletableFuture<T> callLater(@Nonnull Callable<T> task, long delayTicks);

	/**
	 * Executes the given execution after a delay.
	 *
	 * @param task       the task to execute
	 * @param delayTicks the delay in ticks
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> executeLater(@Nonnull Execution task, long delayTicks) {
		return callLater(() -> {
			task.execute();
			return null;
		}, delayTicks);
	}

	/**
	 * Runs the given runnable after a delay.
	 *
	 * @param task       the task to run
	 * @param delayTicks the delay in ticks
	 * @return a future that will be completed when the task is finished
	 */
	@Nonnull
	default CompletableFuture<Void> runLater(@Nonnull Runnable task, long delayTicks) {
		return callLater(() -> {
			task.run();
			return null;
		}, delayTicks);
	}

	/**
	 * Runs a task repeatedly.
	 *
	 * @param task        the task to run
	 * @param delayTicks  the delay in ticks
	 * @param periodTicks the period in ticks
	 * @return a task that can be used to cancel the task
	 */
	@Nonnull
	default Task runRepeating(@Nonnull Runnable task, long delayTicks, long periodTicks) {
		return runRepeating(task, delayTicks, periodTicks, -1);
	}

	/**
	 * Runs a task repeatedly.
	 *
	 * @param task        the task to run
	 * @param delayTicks  the delay in ticks
	 * @param periodTicks the period in ticks
	 * @param cancelTicks the amount of ticks until the task is cancelled (or -1 to never cancel it)
	 * @return a task that can be used to cancel the task
	 */
	@Nonnull
	Task runRepeating(@Nonnull Runnable task, long delayTicks, long periodTicks, long cancelTicks);

	/**
	 * Runs a task repeatedly.
	 *
	 * @param task        the task to run
	 * @param delayTicks  the delay in ticks
	 * @param periodTicks the period in ticks
	 * @return a task that can be used to cancel the task
	 */
	@Nonnull
	default Task runRepeating(@Nonnull Consumer<Task> task, long delayTicks, long periodTicks) {
		return runRepeating(task, delayTicks, periodTicks, -1);
	}

	/**
	 * Runs a task repeatedly.
	 *
	 * @param task        the task to run
	 * @param delayTicks  the delay in ticks
	 * @param periodTicks the period in ticks
	 * @param cancelTicks the amount of ticks until the task is cancelled (or -1 to never cancel it)
	 * @return a task that can be used to cancel the task
	 */
	@Nonnull
	Task runRepeating(@Nonnull Consumer<Task> task, long delayTicks, long periodTicks, long cancelTicks);
}
