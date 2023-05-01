package me.colingrimes.plugin.config;

import me.colingrimes.midnight.config.annotation.Configuration;
import me.colingrimes.midnight.config.option.Message;

import java.util.List;

import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("messages.yml")
public interface Messages {

	/**************************************************
	 *                General Messages                *
	 **************************************************/
	Message<String> PARTICLE_PAGE_HEADER = message("general.particle-page-header", "&a&lParticle Effects &r&7(Page {page}/{total})");
	Message<String> PARTICLE_PAGE_ENTRY = message("general.particle-page-entry", "&7- &a{name} &7| &eType: &a{type} &7| &eLocation: &a{location}");
	Message<List<String>> PARTICLE_PROPERTIES = message("general.particle-properties",
			"&7&m-------------&r &e&lParticle &aProperties &7&m------------",
			"&7Particle: &a{particle}",
			"&7Count: &a{count}",
			"&7Offset: &aX={offset_x} Y={offset_y} Z={offset_z}",
			"&7Speed: &a{speed}",
			"&7Data: &a{data}",
			"&7&m--------------------------------------------"
	);

	/**************************************************
	 *                Success Messages                *
	 **************************************************/
	Message<String> ACTIONBAR_SHOW = message("success.actionbar-show", "&2&l✓ &aShowing Action Bar to &2{player}&a.");
	Message<String> BOSSBAR_SHOW = message("success.bossbar-show", "&2&l✓ &aShowing Boss Bar to &2{player}&a.");
	Message<String> PARTICLE_ATTACH_PLAYER = message("success.particle-attach-player", "&2&l✓ &aParticle has been attached to &2{player}&a.");
	Message<String> PARTICLE_ATTACH_SELF = message("success.particle-attach-player", "&2&l✓ &aParticle has been attached to &2yourself&a.");
	Message<String> PARTICLE_CLEAR = message("success.particle-clear", "&2&l✓ &aAll particles have been cleared.");
	Message<String> PARTICLE_CREATE = message("success.particle-create", "&2&l✓ &aParticle has been created with type &2{type}&a.");
	Message<String> PARTICLE_DELETE = message("success.particle-delete", "&2&l✓ &aParticle has been deleted with the name: &2{name}&a.");
	Message<String> PARTICLE_DETACH_PLAYER = message("success.particle-detach-player", "&2&l✓ &aParticle has been detached from &2{player}&a.");
	Message<String> PARTICLE_DETACH_SELF = message("success.particle-detach-self", "&2&l✓ &aParticle has been detached from &2yourself&a.");
	Message<String> PARTICLE_MODIFY = message("success.particle-modify", "&2&l✓ &aParticle property &2{property}&a has been set to &2{value}&a.");
	Message<String> PARTICLE_MOVE = message("success.particle-move", "&2&l✓ &aParticle has been moved to &2{location}&a.");
	Message<String> PARTICLE_ROTATE = message("success.particle-rotate", "&2&l✓ &aParticle has been rotated.&a.");
	Message<String> PARTICLE_SAVE = message("success.particle-save", "&2&l✓ &aParticle has been saved with the name: &2{name}&a.");
	Message<String> PARTICLE_SELECT = message("success.particle-select", "&2&l✓ &aParticle has been selected with the name: &2{name}&a.");
	Message<String> PARTICLE_SPAWN = message("success.particle-spawn", "&2&l✓ &aParticle has been spawned.");
	Message<String> TITLE_SHOW = message("success.title-show", "&2&l✓ &aShowing Title to &2{player}&a.");

	/**************************************************
	 *                Failure Messages                *
	 **************************************************/
	Message<String> INVALID_LOCATION = message("failure.invalid-location", "&4&l❌ &cThe specified location is invalid.");
	Message<String> INVALID_PAGE = message("failure.invalid-page", "&4&l❌ &cThe specified page is invalid. Defaulting to page 1.");
	Message<String> INVALID_PROPERTY = message("failure.invalid-property", "&4&l❌ &cThe specified property does not exist.");
	Message<String> INVALID_PROPERTY_VALUE = message("failure.invalid-property-value", "&4&l❌ &cThe specified property value is invalid.");
	Message<String> INVALID_ROTATION = message("failure.invalid-rotation", "&4&l❌ &cValid rotations are: &2yaw&a, &2pitch&a, &2roll&a.");
	Message<String> INVALID_SENDER = message("failure.invalid-sender", "&4&l❌ &cThis command can only be executed by a player.");
	Message<String> PARTICLE_NAME_TAKEN = message("failure.particle-name-taken", "&4&l❌ &cThe specified particle name is already taken.");
	Message<String> PARTICLE_NOT_DELETED = message("failure.particle-not-deleted", "&4&l❌ &cThe specified particle has failed to delete.");
	Message<String> PARTICLE_NOT_FOUND = message("failure.particle-not-found", "&4&l❌ &cThe specified particle does not exist.");
	Message<String> PARTICLE_NOT_SAVED = message("failure.particle-not-saved", "&4&l❌ &cThe specified particle has failed to save.");
	Message<String> PARTICLE_NOT_SELECTED = message("failure.particle-not-selected", "&4&l❌ &cYou must select a particle first.");
	Message<String> PERMISSION_DENIED = message("failure.permission-denied", "&4&l❌ &cYou lack the required permission for this command.");
	Message<String> PLAYER_NOT_FOUND = message("failure.player-not-found", "&4&l❌ &cThe specified player is either offline or nonexistent.");

	/**************************************************
	 *                 Usage Messages                 *
	 **************************************************/
	Message<List<String>> ACTIONBAR_USAGE = message("usage.actionbar",
			"&eUsage: &a/actionbar <player> <text> [duration]",
			"&a► &7Show an actionbar message to a player (optional duration)."
	);

	Message<List<String>> BOSSBAR_USAGE = message("usage.bossbar",
			"&eUsage: &a/bossbar <player> <text> [duration]",
			"&a► &7Show an bossbar message to a player (optional duration)."
	);

	Message<List<String>> PARTICLE_USAGE = message("usage.particle",
			"&7&m-------------&r &e&lParticle &aCommands &7&m-------------",
			"&7- &a/particle create &e: &7Create a particle shape.",
			"&7- &a/particle move &e: &7Move a particle to a location.",
			"&7- &a/particle rotate &e: &7Rotate a particle around.",
			"&7- &a/particle modify &e: &7Modify particle properties.",
			"&7- &a/particle properties &e: &7List all particle properties.",
			"&7- &a/particle attach &e: &7Attach particle to a player.",
			"&7- &a/particle detach &e: &7Detach particle from a player.",
			"&7- &a/particle save &e: &7Save particle configuration.",
			"&7- &a/particle delete &e: &7Delete a saved particle.",
			"&7- &a/particle clear &e: &7Clear particles.",
			"&7- &a/particle list &e: &7List all saved particles.",
			"&7- &a/particle select &e: &7Select a saved particle.",
			"&7- &a/particle spawn &e: &7Spawn a particle.",
			"&7&m--------------------------------------------"
	);

	Message<List<String>> PARTICLE_CREATE_USAGE = message("usage.particle-create",
			"&eUsage: &a/particle create <shape> [target]",
			"&a► &7Create a particle shape with an optional target."
	);

	Message<List<String>> PARTICLE_MOVE_USAGE = message("usage.particle-move",
			"&eUsage: &a/particle move [x] [y] [z]",
			"&a► &7Move a particle to a location."
	);

	Message<List<String>> PARTICLE_ROTATE_USAGE = message("usage.particle-rotate",
			"&eUsage: &a/particle rotate <yaw/pitch/roll> <angle>",
			"&a► &7Rotate a particle around."
	);

	Message<List<String>> PARTICLE_MODIFY_USAGE = message("usage.particle-modify",
			"&eUsage: &a/particle modify <prop> <value>",
			"&a► &7Modify a particle property."
	);

	Message<List<String>> PARTICLE_PROPERTIES_USAGE = message("usage.particle-properties",
			"&eUsage: &a/particle properties",
			"&a► &7List all particle properties."
	);

	Message<List<String>> PARTICLE_ATTACH_USAGE = message("usage.particle-attach",
			"&eUsage: &a/particle attach <player>",
			"&a► &7Attach a particle to a player."
	);

	Message<List<String>> PARTICLE_DETACH_USAGE = message("usage.particle-detach",
			"&eUsage: &a/particle detach <player>",
			"&a► &7Detach a particle from a player."
	);

	Message<List<String>> PARTICLE_SAVE_USAGE = message("usage.particle-save",
			"&eUsage: &a/particle save <name>",
			"&a► &7Save a particle configuration."
	);

	Message<List<String>> PARTICLE_DELETE_USAGE = message("usage.particle-delete",
			"&eUsage: &a/particle delete <name>",
			"&a► &7Delete a saved particle."
	);

	Message<List<String>> PARTICLE_CLEAR_USAGE = message("usage.particle-clear",
			"&eUsage: &a/particle clear",
			"&a► &7Clear all particles."
	);

	Message<List<String>> PARTICLE_LIST_USAGE = message("usage.particle-list",
			"&eUsage: &a/particle list",
			"&a► &7List all saved particles."
	);

	Message<List<String>> PARTICLE_SELECT_USAGE = message("usage.particle-select",
			"&eUsage: &a/particle select <name>",
			"&a► &7Select a saved particle by name."
	);

	Message<List<String>> PARTICLE_SPAWN_USAGE = message("usage.particle-spawn",
			"&eUsage: &a/particle spawn [name]",
			"&a► &7Spawn a particle with an optional name."
	);

	Message<List<String>> TITLE_USAGE = message("usage.title",
			"&eUsage: &a/title <player> <title> [subtitle] [fade-in] [stay] [fade-out]",
			"&a► &7Show a title message to a player."
	);
}
