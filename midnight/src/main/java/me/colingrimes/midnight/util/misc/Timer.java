package me.colingrimes.midnight.util.misc;

import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public final class Timer {

    /**
     * Times the execution of a method and logs the result to the console.
     *
     * @param method  the method to time
     * @param message the message to log
     */
    public static <T extends Plugin> void time(@Nonnull T plugin, @Nonnull String message, @Nonnull Runnable method) {
        long startTime = System.nanoTime();
        method.run();
        long endTime = System.nanoTime();

        long elapsedTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        Logger.log(String.format("[%s] %s - Execution Time: %d ms", plugin.getName(), message, elapsedTimeMillis));
    }

    private Timer() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}