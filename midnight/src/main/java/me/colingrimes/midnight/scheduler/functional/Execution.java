package me.colingrimes.midnight.scheduler.functional;

@FunctionalInterface
public interface Execution {

    /**
     * Calls the execution.
     *
     * @throws Exception if an exception occurs
     */
    void execute() throws Exception;
}
