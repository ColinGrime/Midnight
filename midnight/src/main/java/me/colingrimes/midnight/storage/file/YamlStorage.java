package me.colingrimes.midnight.storage.file;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.serialize.Serializable;
import me.colingrimes.midnight.util.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class YamlStorage<T extends Serializable> extends FileStorage<T> {

    protected Map<String, T> dataMap = new HashMap<>();

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
    public void load() {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        dataMap = convertMap(yamlConfiguration.getValues(false));
        loadData(dataMap);
    }

    @Override
    public void save(@Nonnull T data) throws IOException {
        String identifier = getIdentifier(data).orElseThrow(() -> new IllegalStateException("Missing identifier for data."));
        dataMap.put(identifier, data);

        // Serializes and saves the data to the file.
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        for (Map.Entry<String, T> entry : dataMap.entrySet()) {
            for (Map.Entry<String, Object> serializedEntry : entry.getValue().serialize().entrySet()) {
                yamlConfiguration.set(entry.getKey() + "." + serializedEntry.getKey(), serializedEntry.getValue());
            }
        }

        yamlConfiguration.save(file);
    }

    @Override
    public void delete(@Nonnull T data) throws IOException {
        String identifier = getIdentifier(data).orElseThrow(() -> new IllegalStateException("Missing identifier for data."));
        dataMap.remove(identifier);

        // Removes the data from the file.
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set(identifier, null);
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
