package me.colingrimes.example;

import me.colingrimes.example.listener.PlayerListeners;
import me.colingrimes.midnight.plugin.Midnight;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.List;

public class ExamplePlugin extends Midnight {

	@Override
	protected void registerListeners(@Nonnull List<? super Listener> listeners) {
		listeners.add(new PlayerListeners());
	}
}
