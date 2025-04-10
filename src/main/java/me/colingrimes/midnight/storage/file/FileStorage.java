package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.Storage;
import me.colingrimes.midnight.storage.file.composite.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An abstract class representing a file-based storage.
 *
 * @param <T> the type of data being stored
 */
public abstract class FileStorage<T extends Serializable> implements Storage<T> {

    protected final Midnight plugin;
    protected final Class<T> clazz;
    protected final Map<String, File> fileMap;

    public FileStorage(@Nonnull Midnight plugin, @Nonnull Class<T> clazz) {
        this.plugin = plugin;
        this.clazz = clazz;
        this.fileMap = new HashMap<>();
    }

    /**
     * Loads all data from the storage.
     *
     * @throws Exception if there is an issue loading the data
     */
    public abstract void loadAll() throws Exception;

    /**
     * Loads the data associated with the specified composite identifier.
     *
     * @param identifier the composite identifier
     */
    public abstract void load(@Nonnull Identifier identifier) throws Exception;

    /**
     * Processes the data after it has been loaded.
     * <p>
     * When the data is loaded via {@link #loadAll()} or {@link #load(Identifier)},
     * the deserialized data is passed to this method for further processing.
     * <p>
     * This method should be overridden by subclasses so that the data can be
     * loaded into the plugin as necessary.
     *
     * @param data the data to process
     */
    protected abstract void process(@Nonnull T data);

    /**
     * Gets the default file name for the storage.
     * If no default file name is specified, this method should return null.
     * This method should be overridden by subclasses if all data is stored in a single file.
     *
     * @return the default file name
     */
    @Nullable
    protected String getDefaultFileName() {
        return null;
    }

    /**
     * Configures the identifier for the specified data object.
     * By default, the {@code fileName} of the identifier will be set to the default file name.
     * This method should be overridden by subclasses so that it knows where to save/delete the data.
     * <p>
     * Calling the {@link Identifier#setFileName(String)} method allows you to select the file to store the data.
     * Calling the {@link Identifier#setInternalPath(String)} method allows you to select the path within the file to store the data.
     *
     * @param identifier the identifier to configure
     * @param data the data for which the identifier is required
     */
    protected abstract void configureIdentifier(@Nonnull Identifier identifier, @Nonnull T data);

    /**
     * Gets the default file if one exists.
     *
     * @return the default file
     * @throws IOException if there is an issue creating the file
     */
    @Nonnull
    protected Optional<File> getDefaultFile() throws IOException {
        String defaultFileName = getDefaultFileName();
        if (defaultFileName == null) {
            return Optional.empty();
        } else {
            return Optional.of(getFile(defaultFileName, true));
        }
    }

    /**
     * Gets the file associated with the specified file path.
     * If the file does not exist and 'createIfNotExists' is true, it will be created.
     *
     * @param fileName the file name for which the file is required
     * @param createIfNotExists whether to create the file if it does not exist
     * @return the file associated with the file name
     * @throws IOException if there is an issue creating the file
     */
    @Nonnull
    protected File getFile(@Nonnull String fileName, boolean createIfNotExists) throws IOException {
        File file = fileMap.computeIfAbsent(fileName, name -> new File(plugin.getDataFolder(), name));
        if (!file.exists() && createIfNotExists) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    @Override
    public void init() {
        // No specific initialization needed for file-based storage.
    }

    @Override
    public void shutdown() {
        // No specific shutdown operations needed for file-based storage.
    }
}
