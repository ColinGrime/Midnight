```diff
- The Midnight library is currently in development. 
- As a result, there may be numerous bugs and incomplete features. 
- Please use it with caution and report any issues you encounter.
```

# Midnight Library
Midnight is a powerful library for Spigot plugins that simplifies the creation and management of various plugin features.
It focuses on providing a more declarative way to configure and develop your plugins. 
The library currently offers a command management system and a configuration management system using annotations. 

## Command Framework
The command framework simplifies the creation and registration of custom commands, handling command arguments, aliases, permissions, and autocompletion. 
It utilizes annotations such as @Command, @CommandUsage, and @CommandPermission to declare the necessary command information.

Example usage:
```java
@CommandPermission("midnight.give")
@CommandUsage("/midnight give [player] [amount]")
@Command("midnight give")
private void onGive(Player sender, Player player, int amount) {
    ...
}
```

In the above example, it registers the `/midnight give` command. 
You can add any amount of arguments in the method, but it must start with the sender (Player or CommandSender). 
If the player types `/midnight give` but the specified arguments are not found, the command usage message is displayed to the user.

## Configuration Management System
The configuration management system allows for easy creation and management of plugin configurations using annotations. 
This system provides a more declarative way to create and manage configurations, using option() and message() methods.

Example usage:
```java
import static me.colingrimes.midnight.config.option.OptionFactory.*;

@Configuration("settings.yml")
public interface Settings {

    Message<String> PERMISSION_DENIED = message("no-permission", "&cYou do not have permission.");
    Message<String> INVALID_SENDER = message("invalid-sender", "&cYou must be a player to use this command.");
    Message<String> WELCOME_MESSAGE = message("welcome-message", "&aWelcome to the server, %player%!");

    Option<Integer> EXAMPLE_NUMBER = option("exampleNumber", 5);
    Option<Boolean> EXAMPLE_BOOLEAN = option("exampleBoolean", true);
}
```

In the example above, a configuration file named `settings.yml` is created with **3** messages and **2** options.
* Messages provide a convenient way to send messages directly to the sender using the `sendTo(sender)` method.
* Options enable easy retrieval of the stored values using the `option.get()` method.
