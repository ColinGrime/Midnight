package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.Storage;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class representing a file-based storage.
 * @param <T> the type of data being stored
 */
public abstract class FileStorage<T extends Serializable> implements Storage<T> {

    protected final MidnightPlugin plugin;
    protected File file;

    public FileStorage(@Nonnull MidnightPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() throws Exception {
        Optional<String> identifier = getIdentifier(null);
        if (identifier.isPresent()) {
            file = new File(plugin.getDataFolder(), identifier.get());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }
    }

    @Override
    public void shutdown() {
        // No specific shutdown operations needed for file-based storage.
    }

    /**
     * Loads the data from the in-memory data map.
     * Implementations of this method should perform any necessary
     * post-processing on the data after it has been loaded.
     *
     * @param dataMap the in-memory data map containing the loaded data
     */
    protected abstract void loadData(@Nonnull Map<String, T> dataMap);
}
