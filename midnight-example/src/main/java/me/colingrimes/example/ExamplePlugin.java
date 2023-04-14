package me.colingrimes.example;

import me.colingrimes.example.listener.PlayerListeners;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class ExamplePlugin extends MidnightPlugin {

	@Override
	protected void registerListeners(@Nonnull List<? super Listener> listeners) {
		listeners.add(new PlayerListeners());
	}
}
