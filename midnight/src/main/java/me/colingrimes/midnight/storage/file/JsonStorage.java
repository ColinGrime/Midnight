package me.colingrimes.midnight.storage.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.serialize.Serializable;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonStorage<T extends Serializable> extends FileStorage<T> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected Map<String, T> dataMap = new HashMap<>();

    public JsonStorage(@Nonnull MidnightPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load() throws IOException {
        try (FileReader reader = new FileReader(file)) {
            dataMap = gson.fromJson(reader, new TypeToken<Map<String, T>>(){}.getType());
            if (dataMap == null) {
                dataMap = new HashMap<>();
            }
            loadData(dataMap);
        }
    }

    @Override
    public void save(@Nonnull T data) throws IOException {
        dataMap.put(getIdentifier(data).orElseThrow(() -> new IllegalStateException("Missing identifier for data.")), data);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(dataMap, writer);
        }
    }

    @Override
    public void delete(@Nonnull T data) throws IOException {
        dataMap.remove(getIdentifier(data).orElseThrow(() -> new IllegalStateException("Missing identifier for data.")));
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(dataMap, writer);
        }
    }
}
