package me.colingrimes.midnight.update;

import me.colingrimes.midnight.util.io.Logger;

/**
 * Responsible for looking up if there are any updates available from the plugin.
 */
public interface UpdateChecker {

	/**
	 * Checks for any available updates from the plugin.
	 * <p>
	 * If a new update is available, it will call {@link Logger#warn(String)} with the resource URL.
	 */
	void check();
}
