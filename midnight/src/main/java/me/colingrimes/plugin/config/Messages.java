package me.colingrimes.plugin.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Message;

import java.util.List;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("messages.yml")
public interface Messages {

	Message<String> PERMISSION_DENIED = message("no-permission", "&cYou do not have permission.");
	Message<String> INVALID_SENDER = message("invalid-sender", "&cYou must be a player to use this command.");

	/**************************************************
	 *                 Particle Stuff                 *
	 **************************************************/
	Message<List<String>> PARTICLE_USAGE = message("usage.particle",
			"&7&m------------- &e&lParticle &aCommands &7&m-------------",
			"&7- &a/particle create &e: &7Create a particle shape.",
			"&7- &a/particle select &e: &7Select a saved particle.",
			"&7- &a/particle modify &e: &7Modify particle properties.",
			"&7- &a/particle preview &e: &7Preview particle location.",
			"&7- &a/particle attach &e: &7Attach particle to a player.",
			"&7- &a/particle detach &e: &7Detach particle from a player.",
			"&7- &a/particle save &e: &7Save particle configuration.",
			"&7- &a/particle load &e: &7Load a saved particle.",
			"&7- &a/particle remove &e: &7Remove a saved particle.",
			"&7- &a/particle clear &e: &7Clear all particles.",
			"&7&m--------------------------------------------"
	);

	Message<List<String>> PARTICLE_CREATE_USAGE = message("usage.particle-create",
			"&eUsage: &a/particle create <shape> [target]",
			"&a► &7Create a particle shape with an optional target."
	);

	Message<List<String>> PARTICLE_SELECT_USAGE = message("usage.particle-select",
			"&eUsage: &a/particle select <name>",
			"&a► &7Select a saved particle by name."
	);

	Message<List<String>> PARTICLE_MODIFY_USAGE = message("usage.particle-modify",
			"&eUsage: &a/particle modify <prop> <value>",
			"&a► &7Modify a particle property."
	);

	Message<List<String>> PARTICLE_PREVIEW_USAGE = message("usage.particle-preview",
			"&eUsage: &a/particle preview",
			"&a► &7Toggle particle location preview."
	);

	Message<List<String>> PARTICLE_ATTACH_USAGE = message("usage.particle-attach",
			"&eUsage: &a/particle attach <player>",
			"&a► &7Attach a particle to a player."
	);

	Message<List<String>> PARTICLE_DETACH_USAGE = message("usage.particle-detach",
			"&eUsage: &a/particle detach",
			"&a► &7Detach a particle from a player."
	);

	Message<List<String>> PARTICLE_SAVE_USAGE = message("usage.particle-save",
			"&eUsage: &a/particle save <name>",
			"&a► &7Save a particle configuration."
	);

	Message<List<String>> PARTICLE_LOAD_USAGE = message("usage.particle-load",
			"&eUsage: &a/particle load <name> [target]",
			"&a► &7Load a saved particle with an optional target."
	);

	Message<List<String>> PARTICLE_REMOVE_USAGE = message("usage.particle-remove",
			"&eUsage: &a/particle remove <name>",
			"&a► &7Remove a saved particle."
	);

	Message<List<String>> PARTICLE_CLEAR_USAGE = message("usage.particle-clear",
			"&eUsage: &a/particle clear",
			"&a► &7Clear all particles."
	);
}
