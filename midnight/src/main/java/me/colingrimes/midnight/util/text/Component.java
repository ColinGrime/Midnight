package me.colingrimes.midnight.util.text;

import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.message.implementation.ComponentMessage;
import me.colingrimes.midnight.message.implementation.ListMessage;
import me.colingrimes.midnight.message.implementation.TextMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import javax.annotation.Nonnull;

public final class Component {

    /**
     * Replaces all instances of the placeholder with the value in the specified component.
     *
     * @param component   the component to replace the placeholder in
     * @param placeholder the placeholder
     * @param value       the value
     * @return the component with the placeholder replaced
     */
    @Nonnull
    public static TextComponent replace(@Nonnull TextComponent component, @Nonnull String placeholder, @Nonnull Message<?> value) {
        TextComponent mainComponent = new TextComponent();

        if (component.getText().contains(placeholder)) {
            String[] parts = component.getText().split(placeholder);

            for (int i=0; i<parts.length; i++) {
                TextComponent partComponent = new TextComponent(parts[i]);

                // Copy the original component's events to each part.
                partComponent.setClickEvent(component.getClickEvent());
                partComponent.setHoverEvent(component.getHoverEvent());
                mainComponent.addExtra(partComponent);

                // Don't add the replacement value after the last part.
                if (i < parts.length - 1) {
                    if (value instanceof TextMessage val) {
                        mainComponent.addExtra(val.getContent());
                    } else if (value instanceof ComponentMessage val) {
                        mainComponent.addExtra(val.getContent());
                    } else if (value instanceof ListMessage val) {
                        TextComponent main = mainComponent;
                        val.getContent().forEach(msg -> main.addExtra(new TextComponent(msg)));
                    }
                }
            }
        } else {
            mainComponent = new TextComponent(component);
        }

        // Recursively replace placeholders in the extras of the original component.
        if (component.getExtra() != null) {
            for (BaseComponent baseComponent : component.getExtra()) {
                if (baseComponent instanceof TextComponent extraComponent) {
                    mainComponent.addExtra(replace(extraComponent, placeholder, value));
                } else {
                    mainComponent.addExtra(baseComponent);
                }
            }
        }

        return mainComponent;
    }

    private Component() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
