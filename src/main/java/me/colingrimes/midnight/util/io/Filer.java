package me.colingrimes.midnight.util.io;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class Filer {

	/**
	 * Deletes the given file.
	 * If the file is a directory, it will <b>recursively delete all files and subdirectories.</b>
	 *
	 * @param file the file
	 * @return true if the file was deleted
	 */
	public static boolean deleteRecursively(@Nullable File file) {
		if (file == null || !file.exists()) {
			return false;
		} else if (!file.isDirectory()) {
			return file.delete();
		}

		File[] files = file.listFiles();
		if (files == null) {
			return file.delete();
		}

		return Stream.of(files).allMatch(Filer::deleteRecursively) && file.delete();
	}

	/**
	 * Duplicates the given source file to the target location.
	 * If the file is a directory, it will recursively duplicate all files and subdirectories.
	 *
	 * @param source     the source file or directory
	 * @param targetPath the target path where source should be duplicated
	 * @return true if the duplication was successful
	 */
	public static boolean duplicate(@Nullable File source, @Nonnull Path targetPath) {
		if (source == null || !source.exists()) {
			return false;
		}

		if (!source.isDirectory()) {
			try {
				Files.copy(source.toPath(), targetPath);
				return true;
			} catch (IOException e) {
				Logger.severe("[Midnight] Filer has failed to duplicate file '" + source.getAbsolutePath() + "':", e);
				return false;
			}
		}

		File target = targetPath.toFile();
		if (!target.exists() && !target.mkdir()) {
			return false;
		}

		File[] files = source.listFiles();
		if (files == null) {
			return false;
		}

		return Stream.of(files).allMatch(file -> duplicate(file, targetPath.resolve(file.getName())));
	}

	/**
	 * Zips the provided source and saves the zipped file to the specified target path.
	 *
	 * @param source     the file or directory to zip
	 * @param targetPath the path where the zipped file will be saved
	 * @return true if the zipping operation was successful
	 */
	public static boolean zip(@Nullable File source, @Nonnull Path targetPath) {
		if (source == null || !source.exists()) {
			return false;
		}

		// Ensure the parent directory for the output zip exists.
		targetPath.getParent().toFile().mkdirs();

		try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(targetPath));
			 Stream<Path> paths = Files.walk(source.toPath())) {
			paths.forEach(path -> {
				try {
					zos.putNextEntry(new ZipEntry(source.toPath().relativize(path).toString()));
					if (!Files.isDirectory(path)) {
						Files.copy(path, zos);
					}
					zos.closeEntry();
				} catch (IOException e) {
					Logger.severe("[Midnight] Filer has failed to zip file '" + path + "':", e);
				}
			});
		} catch (IOException e) {
			Logger.severe("[Midnight] Filer has failed to zip file '" + source.getAbsolutePath() + "':", e);
			return false;
		}

		return true;
	}

	private Filer() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}
