package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;
import me.colingrimes.midnight.util.io.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class YamlStorage<T extends Serializable> extends FileStorage<T> {

    public YamlStorage(@Nonnull MidnightPlugin plugin) {
        super(plugin);
    }

    /**
     * Returns the deserialization function for the specific data type.
     * @return the deserialization function
     */
    @Nonnull
    protected abstract Function<Map<String, Object>, T> getDeserializationFunction();

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
        Map<String, Object> rawData = convertToRawData(sec);
        convertMap(rawData).values().forEach(this::process);
    }

    /**
     * Recursively converts a memory section to a map.
     * @param section the memory section
     * @return the map
     */
    private Map<String, Object> convertToRawData(@Nonnull ConfigurationSection section) {
        Map<String, Object> map = new HashMap<>();
        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            if (value instanceof ConfigurationSection) {
                map.put(key, convertToRawData((ConfigurationSection) value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public void load(@Nonnull String identifier) throws IOException {
        File file = getFile(identifier, false);
        if (!file.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection sec = config.getConfigurationSection(identifier);
        if (sec == null) {
            return;
        }

        // Process the file of the specified identifier.
        Map<String, Object> rawData = sec.getValues(false);
        process(getDeserializationFunction().apply(rawData));
    }

    @Override
    public void save(@Nonnull T data) throws IOException {
        Optional<CompositeIdentifier> identifier = getIdentifier(data);
        if (identifier.isEmpty()) {
            return;
        }

        File file = getFile(identifier.get().getFilePath(), true);
        String path = identifier.get().getInternalPath();

        // Serialize the data.
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Object> serialized : data.serialize().entrySet()) {
            config.set(path + "." + serialized.getKey(), serialized.getValue());
        }

        config.save(file);
    }

    @Override
    public void delete(@Nonnull T data) throws IOException {
        Optional<CompositeIdentifier> identifier = getIdentifier(data);
        if (identifier.isEmpty() || identifier.get().getInternalPath() == null) {
            return;
        }

        File file = getFile(identifier.get().getFilePath(), false);
        String path = identifier.get().getInternalPath();

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
     * Converts a raw data map from the YAML configuration to a data map with the appropriate types.
     * This method calls the deserialize method with the deserialization function provided by the
     * getDeserializationFunction() method.
     *
     * @param map the raw data map from the YAML configuration
     * @return the converted data map with appropriate types
     */
    @Nonnull
    private Map<String, T> convertMap(@Nonnull Map<String, Object> map) {
        Map<String, T> convertedMap = new HashMap<>();
        Function<Map<String, Object>, T> deserializationFunction = getDeserializationFunction();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            // Only deserialize maps (there should only be maps in the data map).
            if (!(entry.getValue() instanceof Map)) {
                Logger.warn("Invalid data type for key: " + entry.getKey());
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> serialized = (Map<String, Object>) entry.getValue();

            try {
                T deserialized = Serializable.deserialize(serialized, deserializationFunction);
                convertedMap.put(entry.getKey(), deserialized);
            } catch (RuntimeException e) {
                Logger.warn("Error during deserialization of key: " + entry.getKey());
                e.printStackTrace();
            }
        }

        return convertedMap;
    }
}
