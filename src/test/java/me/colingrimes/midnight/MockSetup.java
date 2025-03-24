package me.colingrimes.midnight;

import me.colingrimes.midnight.plugin.LoadingPlugin;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.scheduler.implementation.AsyncScheduler;
import me.colingrimes.midnight.scheduler.implementation.SyncScheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.Server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public abstract class MockSetup {

	// Time constants to test Instant.now() calls.
	protected static final Instant INSTANT_1 = Instant.now();
	protected static final Instant INSTANT_2 = Instant.now().plus(Duration.ofSeconds(5));
	protected static final Instant INSTANT_3 = Instant.now().plus(Duration.ofSeconds(10));

	// Common mocks for all tests.
	protected Server server;
	protected Midnight plugin;
	protected BukkitMocks bukkit;
	@Mock protected SyncScheduler syncScheduler;
	@Mock protected AsyncScheduler asyncScheduler;

	// Common static mocks for all tests.
	@Mock protected MockedStatic<MidnightPlugin> midnight;
	@Mock protected MockedStatic<Common> common;
	@Mock protected MockedStatic<Players> players;
	@Mock protected MockedStatic<Scheduler> scheduler;
	protected MockedStatic<Instant> instant;

	@BeforeEach
	void setUp() {
		server = MockBukkit.mock();
		plugin = MockBukkit.load(LoadingPlugin.class);
		bukkit = new BukkitMocks();

		// Mock the MidnightPlugin.get() call.
		midnight.when(MidnightPlugin::get).thenReturn(plugin);

		// Mock the Common.server(), Common.plugin(), and Common.logger() calls.
		common.when(Common::server).thenReturn(server);
		common.when(() -> Common.plugin(any())).thenReturn(null);
		common.when(Common::logger).thenReturn(mock(Logger.class));

		// Mock the Players.get() call to return the player mock.
		players.when(() -> Players.get((String) any())).thenAnswer(invocation -> {
			String name = invocation.getArgument(0);
			return name.equalsIgnoreCase(bukkit.player.getName()) ? Optional.of(bukkit.player) : Optional.empty();
		});
		players.when(() -> Players.get((UUID) any())).thenAnswer(invocation -> {
			UUID uuid = invocation.getArgument(0);
			return uuid.equals(bukkit.player.getUniqueId()) ? Optional.of(bukkit.player) : Optional.empty();
		});

		// Mock the Scheduler.sync() and Scheduler.async() calls.
		scheduler.when(Scheduler::sync).thenReturn(syncScheduler);
		scheduler.when(Scheduler::async).thenReturn(asyncScheduler);

		// Mock the Instant.now() call to be the current time.
		instant = mockStatic(Instant.class, CALLS_REAL_METHODS);
		instant.when(Instant::now).thenReturn(INSTANT_1);
	}

	@AfterEach
	void tearDown() throws Exception {
		MockBukkit.unmock();
		bukkit.close();
		instant.close();
	}
}
