package me.colingrimes.midnight.util.bukkit;

import me.colingrimes.midnight.util.io.Filer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Worlds {

	/**
	 * Creates a world.
	 *
	 * @param worldCreator the world creator
	 * @return the created world
	 */
	@Nonnull
	public static World create(@Nonnull WorldCreator worldCreator) {
		return Objects.requireNonNull(Bukkit.createWorld(worldCreator));
	}

	/**
	 * Deletes a world by name.
	 *
	 * @param name the name of the world
	 * @return true if the world was deleted or did not exist
	 */
	public static boolean delete(@Nonnull String name) {
		if (!exists(name)) {
			return true;
		} else if (get(name).isEmpty()) {
			return Filer.deleteRecursively(worldFolder(name));
		}

		Players.forEach(p -> p.teleport(loaded().get(0).getSpawnLocation()));
		return unload(name, false) && Filer.deleteRecursively(worldFolder(name));
	}


	/**
	 * Duplicates a world by its name.
	 * Once duplicated, the world is loaded into memory.
	 *
	 * @param source the name of the world to be duplicated
	 * @param target the name of the duplicated world
	 * @return true if the world was successfully duplicated and loaded into memory
	 */
	public static boolean duplicate(@Nonnull String source, @Nonnull String target) {
		if (!exists(source) || exists(target)) {
			return false;
		}

		File sourceDirectory = worldFolder(source);
		Path targetPath = Paths.get(container().getAbsolutePath(), target);
		return Filer.duplicate(sourceDirectory, targetPath) && load(target).isPresent();
	}

	/**
	 * Zips the specified world and saves the zipped file in the server directory.
	 *
	 * @param name the name of the world to zip
	 * @return true if the operation was successful
	 */
	public static boolean zip(@Nonnull String name) {
		File worldDirectory = worldFolder(name);
		Path zipOutputPath = Paths.get(container().getAbsolutePath(), name + ".zip");
		return Filer.zip(worldDirectory, zipOutputPath);
	}

	/**
	 * Gets a world by name.
	 *
	 * @param name the name of the world
	 * @return the world, if it exists
	 */
	@Nonnull
	public static Optional<World> get(@Nonnull String name) {
		return Optional.ofNullable(Bukkit.getWorld(name));
	}

	/**
	 * Gets the name of all the worlds on the server, including unloaded worlds.
	 *
	 * @return the names of all worlds
	 */
	@Nonnull
	public static List<String> all() {
		return all(true);
	}

	/**
	 * Gets the name of all the worlds on the server.
	 *
	 * @param includeUnloaded whether to include unloaded worlds
	 * @return the names of all worlds
	 */
	@Nonnull
	public static List<String> all(boolean includeUnloaded) {
		if (!includeUnloaded) {
			return loaded().stream().map(World::getName).collect(Collectors.toList());
		}

		File[] worldFiles = container().listFiles(Worlds::exists);
		if (worldFiles != null) {
			return Stream.of(worldFiles).map(File::getName).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Gets all the loaded worlds on the server.
	 *
	 * @return all loaded worlds
	 */
	public static List<World> loaded() {
		return Bukkit.getWorlds();
	}

	/**
	 * Loads a world into memory by name.
	 *
	 * @param name the name of the world
	 * @return the world if it exists
	 */
	@Nonnull
	public static Optional<World> load(@Nonnull String name) {
		if (get(name).isPresent()) {
			return get(name);
		} else if (exists(name)) {
			return Optional.ofNullable(Bukkit.createWorld(new WorldCreator(name)));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Unloads a world from memory by name.
	 * Before unloading, the world is saved.
	 *
	 * @param name the name of the world
	 * @return true if the world was unloaded
	 */
	public static boolean unload(@Nonnull String name) {
		return unload(name, true);
	}

	/**
	 * Unloads a world from memory by name.
	 *
	 * @param name the name of the world
	 * @param save whether to save the world
	 * @return true if the world was unloaded
	 */
	public static boolean unload(@Nonnull String name, boolean save) {
		return Bukkit.unloadWorld(name, save);
	}

	/**
	 * Unloads a world from memory.
	 * Before unloading, the world is saved.
	 *
	 * @param world the world
	 * @return true if the world was unloaded
	 */
	public static boolean unload(@Nonnull World world) {
		return unload(world, true);
	}

	/**
	 * Unloads a world from memory.
	 *
	 * @param world the world
	 * @param save whether to save the world
	 * @return true if the world was unloaded
	 */
	public static boolean unload(@Nonnull World world, boolean save) {
		return Bukkit.unloadWorld(world, save);
	}

	/**
	 * Checks if a world exists by name.
	 *
	 * @param name the name of the world
	 * @return true if the world exists
	 */
	public static boolean exists(@Nonnull String name) {
		return get(name).isPresent() || exists(worldFolder(name));
	}

	/**
	 * Checks if a world exists by file.
	 *
	 * @param worldFile the world file
	 * @return true if the world exists
	 */
	public static boolean exists(@Nullable File worldFile) {
		return worldFile != null && worldFile.exists() && worldFile.isDirectory() && new File(worldFile, "level.dat").exists();
	}

	/**
	 * Gets the container of all worlds.
	 *
	 * @return the world container
	 */
	@Nonnull
	public static File container() {
		return Bukkit.getWorldContainer();
	}

	/**
	 * Gets the folder of a world by name.
	 *
	 * @param name the name of the world
	 * @return the world folder or null if it does not exist
	 */
	@Nullable
	public static File worldFolder(@Nonnull String name) {
		File file = new File(container(), name);
		return exists(file) ? file : null;
	}

	private Worlds() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
