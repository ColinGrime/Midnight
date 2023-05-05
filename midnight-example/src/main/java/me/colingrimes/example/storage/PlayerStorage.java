package me.colingrimes.example.storage;

import me.colingrimes.example.ExamplePlugin;
import me.colingrimes.example.player.PlayerData;
import me.colingrimes.midnight.storage.file.YamlStorage;
import me.colingrimes.midnight.storage.file.composite.CompositeIdentifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public class PlayerStorage extends YamlStorage<PlayerData> {

    private final ExamplePlugin plugin;

    public PlayerStorage(@Nonnull ExamplePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    protected void process(@Nonnull PlayerData data) {
        plugin.getPlayerManager().loadPlayerData(data.getUUID(), data);
    }

    @Nullable
    @Override
    protected CompositeIdentifier getIdentifier(@Nullable PlayerData data) {
        if (data == null) {
            return null;
        } else {
            return CompositeIdentifier.of("player-data/" + data.getUUID());
        }
    }

    @Nonnull
    @Override
    protected Function<Map<String, Object>, PlayerData> getDeserializationFunction() {
        return PlayerData::deserialize;
    }
}
