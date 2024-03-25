package me.colingrimes.midnight.cache;

import me.colingrimes.midnight.MockSetup;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CooldownTest extends MockSetup {

	@Mock private Consumer<Player> defaultAction;
	@Mock private Consumer<Player> action;
	private Runnable task;

	@BeforeEach
	void setUp() {
		// Capture the Scheduler.sync().runRepeating() Runnable argument.
		when(syncScheduler.runRepeating(any(), anyLong(), anyLong())).thenAnswer(invocation -> {
			task = invocation.getArgument(0);
			return null;
		});
	}

	@Test
	void testCooldown() {
		// Test basic adding, expiration checking, and cancelling.
		Cooldown<Player> playerCooldown = Cooldown.create(Duration.ofSeconds(10), defaultAction);
		assertFalse(playerCooldown.onCooldown(bukkit.player));
		playerCooldown.add(bukkit.player);
		assertTrue(playerCooldown.onCooldown(bukkit.player));
		playerCooldown.cancel(bukkit.player);
		assertFalse(playerCooldown.onCooldown(bukkit.player));
		playerCooldown.add(bukkit.player);
		assertTrue(playerCooldown.onCooldown(bukkit.player));

		// Test that the action was not executed yet.
		task.run();
		assertTrue(playerCooldown.onCooldown(bukkit.player));
		verify(defaultAction, never()).accept(bukkit.player);

		// Test that the action was still not executed after 5 seconds.
		instant.when(Instant::now).thenReturn(INSTANT_2);
		task.run();
		assertTrue(playerCooldown.onCooldown(bukkit.player));
		verify(defaultAction, never()).accept(bukkit.player);

		// Test that the action was executed after the cooldown period.
		instant.when(Instant::now).thenReturn(INSTANT_3);
		task.run();
		assertFalse(playerCooldown.onCooldown(bukkit.player));
		verify(defaultAction).accept(bukkit.player);
	}

	@Test
	void testCooldownSpecific() {
		Cooldown<Player> playerCooldown = Cooldown.create(Duration.ofSeconds(10), defaultAction);
		playerCooldown.add(bukkit.player, Duration.ofSeconds(5), action);
		assertTrue(playerCooldown.onCooldown(bukkit.player));

		// Test that the specific action was executed after the specific cooldown period.
		instant.when(Instant::now).thenReturn(INSTANT_2);
		task.run();
		assertFalse(playerCooldown.onCooldown(bukkit.player));
		verify(action).accept(bukkit.player);
	}

	@Test
	void testCooldownNoAction() {
		Cooldown<Player> playerCooldown = Cooldown.create(Duration.ofSeconds(10));
		playerCooldown.add(bukkit.player, Duration.ofSeconds(5), action);
		playerCooldown.add(bukkit.player2, Duration.ofSeconds(5));
		assertTrue(playerCooldown.onCooldown(bukkit.player));
		assertTrue(playerCooldown.onCooldown(bukkit.player2));

		// Test that no action was executed for player 2 after the cooldown period.
		instant.when(Instant::now).thenReturn(INSTANT_2);
		task.run();
		assertFalse(playerCooldown.onCooldown(bukkit.player));
		assertFalse(playerCooldown.onCooldown(bukkit.player2));
		verify(defaultAction, never()).accept(bukkit.player);
		verify(action).accept(bukkit.player);
		verify(defaultAction, never()).accept(bukkit.player2);
		verify(action, never()).accept(bukkit.player2);
	}
}
