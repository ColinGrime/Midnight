package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.serialize.Json;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.file.composite.Identifier;
import me.colingrimes.midnight.storage.file.exception.FileNotSpecifiedException;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class YamlStorage<T extends Serializable> extends FileStorage<T> {

    public YamlStorage(@Nonnull Midnight plugin, @Nonnull Class<T> clazz) {
        super(plugin, clazz);
    }

    @Override
    public void loadAll() throws IOException {
        Optional<File> defaultFile = getDefaultFile();
        if (defaultFile.isEmpty()) {
            Logger.severe("Default files are not setup for this plugin. Please do not use loadAll().");
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(defaultFile.get());
        ConfigurationSection sec = config.getConfigurationSection("");
        if (sec == null) {
            return;
        }

        // Process the default file.
        for (String key : sec.getKeys(false)) {
            String json = sec.getString(key);
            if (json != null) {
                process(Serializable.deserialize(clazz, Json.toElement(json)));
            }
        }
    }

    @Override
    public void load(@Nonnull Identifier identifier) throws Exception {
        File file = getFile(getFileName(identifier), false);
        if (!file.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String json = config.getString(identifier.getInternalPath());
        if (json == null) {
            return;
        }

        // Process the file of the specified identifier.
        process(Serializable.deserialize(clazz, Json.toElement(json)));
    }

    @Override
    public void save(@Nonnull T data) throws Exception {
        Identifier identifier = Identifier.create();
        configureIdentifier(identifier, data);

        // Gets the file and internal path.
        File file = getFile(getFileName(identifier), true);
        String path = getInternalPath(identifier);

        // Serialize the data.
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(path, Json.toString(data));
        config.save(file);
    }

    @Override
    public void delete(@Nonnull T data) throws Exception {
        Identifier identifier = Identifier.create();
        configureIdentifier(identifier, data);

        // Gets the file and internal path.
        File file = getFile(getFileName(identifier), false);
        String path = identifier.getInternalPath();

        // Delete the file if it is a unique file.
        if (!file.equals(getDefaultFile().orElse(null)) && file.delete()) {
            return;
        }

        // Otherwise, delete the data from the file.
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set(path, null);
        yamlConfiguration.save(file);
    }

    /**
     * Gets the name of the YAML file for the specified identifier.
     * <p>
     * If the file name is not specified, the default file name is set and used.
     *
     * @param identifier the identifier
     * @return the YAML file name
     */
    @Nonnull
    private String getFileName(@Nonnull Identifier identifier) throws FileNotSpecifiedException {
        if (identifier.getFileName() == null) {
            identifier.setFileName(getDefaultFileName());
        }

        String fileName = identifier.getFileName();
        if (fileName == null) {
            throw new FileNotSpecifiedException();
        } else {
            return fileName.endsWith(".yml") ? fileName : fileName + ".yml";
        }
    }

    /**
     * Gets the internal path for the specified identifier.
     *
     * @param identifier the identifier
     * @return the internal path
     */
    private String getInternalPath(@Nonnull Identifier identifier) {
        String internalPath = identifier.getInternalPath();
        return internalPath.isEmpty() ? "" : internalPath + ".";
    }
}
