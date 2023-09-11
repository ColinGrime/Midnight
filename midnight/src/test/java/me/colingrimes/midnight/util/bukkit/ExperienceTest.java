package me.colingrimes.midnight.util.bukkit;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceTest {

	@Mock private Player player;

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
		assertEquals(55, totalExp);
	}

	@Test
	void testFromLevel() {
		int totalExp = Experience.fromLevel(10);
		assertEquals(160, totalExp);
	}

	@Test
	void testToLevel() {
		double level = Experience.toLevel(216L);
		assertEquals(12, level, 1e-9);
	}

	@Test
	void testToWholeLevel() {
		int level = Experience.toWholeLevel(1095L);
		assertEquals(27, level);
	}
}
