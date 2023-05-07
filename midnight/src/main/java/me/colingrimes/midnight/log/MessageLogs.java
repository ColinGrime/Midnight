package me.colingrimes.midnight.log;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MessageLogs<T, M> implements Logs<T, M> {

    private final Map<T, List<M>> logs = new HashMap<>();

    @Nonnull
    @Override
    public List<M> getLogs(@Nonnull T key) {
        return logs.get(key);
    }

    @Override
    public void addLog(@Nonnull T key, @Nonnull M message) {
        logs.get(key).add(message);
    }

    @Override
    public void clearLogs(@Nonnull T key) {
        logs.get(key).clear();
    }
}
