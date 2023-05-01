package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class representing a file-based storage.
 * @param <T> the type of data being stored
 */
public abstract class FileStorage<T extends Serializable> implements Storage<T> {

    protected final MidnightPlugin plugin;
    protected Map<String, File> fileMap;

    public FileStorage(@Nonnull MidnightPlugin plugin) {
        this.plugin = plugin;
        this.fileMap = new HashMap<>();
    }

    @Override
    public void init() {
        // No specific initialization needed for file-based storage.
    }

    @Override
    public void shutdown() {
        // No specific shutdown operations needed for file-based storage.
    }

    /**
     * Processes the data after it has been loaded.
     * @param data the data to process
     */
    protected abstract void process(@Nonnull T data);

    /**
     * Gets the composite identifier for the specified data object.
     * @param data the data for which the identifier is required
     * @return the composite identifier
     */
    @Nonnull
    protected abstract Optional<CompositeIdentifier> getIdentifier(@Nullable T data);

    /**
     * Gets the file associated with the specified file path.
     * If the file does not exist and 'createIfNotExists' is true, it will be created.
     *
     * @param filePath the file path for which the file is required
     * @param createIfNotExists whether to create the file if it does not exist
     * @return the file associated with the file path
     * @throws IOException if there is an issue creating the file
     */
    @Nonnull
    protected File getFile(@Nonnull String filePath, boolean createIfNotExists) throws IOException {
        File file = fileMap.computeIfAbsent(filePath, path -> new File(plugin.getDataFolder(), path));
        if (!file.exists() && createIfNotExists) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    /**
     * Gets the default file if one exists.
     * @return the default file
     * @throws IOException if there is an issue creating the file
     */
    @Nonnull
    protected Optional<File> getDefaultFile() throws IOException {
        Optional<CompositeIdentifier> identifier = getIdentifier(null);
        if (identifier.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(getFile(identifier.get().getFilePath(), true));
        }
    }
}
