package me.colingrimes.midnight.util.player;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExperienceTest {

	private Player player;

	@BeforeEach
	void setUp() {
		player = Mockito.mock(Player.class);
	}

	@Test
	void testAddExperience() {
		when(player.getLevel()).thenReturn(10);
		when(player.getExp()).thenReturn(0.5f);

		Experience.add(player, 500);

		verify(player).setLevel(anyInt());
		verify(player).setExp(anyFloat());
	}

	@Test
	void testRemoveExperience() {
		when(player.getLevel()).thenReturn(15);
		when(player.getExp()).thenReturn(0.5f);

		Experience.remove(player, 500);

		verify(player).setLevel(anyInt());
		verify(player).setExp(anyFloat());
	}

	@Test
	void testFromPlayer() {
		when(player.getLevel()).thenReturn(5);

		int totalExp = Experience.fromPlayer(player);
		assertEquals(55, totalExp, "Total experience calculated from player is incorrect.");
	}

	@Test
	void testFromLevel() {
		int totalExp = Experience.fromLevel(10);
		assertEquals(160, totalExp, "Total experience calculated from level is incorrect.");
	}

	@Test
	void testToLevel() {
		double level = Experience.toLevel(216L);
		assertEquals(12, level, 1e-9, "Level calculated from total experience is incorrect.");
	}

	@Test
	void testToWholeLevel() {
		int level = Experience.toWholeLevel(1095L);
		assertEquals(27, level, "Whole level calculated from total experience is incorrect.");
	}
}
