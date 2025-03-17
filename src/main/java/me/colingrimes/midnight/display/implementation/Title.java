package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.display.Display;
import me.colingrimes.midnight.display.type.DisplayType;
import me.colingrimes.midnight.event.DisplayHideEvent;
import me.colingrimes.midnight.event.DisplayShowEvent;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Title implements Display {

    private String id;
    private String title;
    private String subtitle;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;

    public Title(@Nullable String title) {
        this(title, null);
    }

    public Title(@Nullable String title, @Nullable String subtitle) {
        this.title = Text.color(title);
        this.subtitle = Text.color(subtitle);
        this.fadeInTime = 20;  // Default 1 second.
        this.stayTime = 60;    // Default 3 seconds.
        this.fadeOutTime = 20; // Default 1 second.
    }

    @Nonnull
    @Override
    public DisplayType getType() {
        return DisplayType.TITLE;
    }

    @Nonnull
    @Override
    public String getText() {
        return title;
    }

    @Override
    public void setText(@Nonnull String text) {
        this.title = Text.color(text);
    }

    @Override
    public void show(@Nonnull Player player) {
        DisplayShowEvent displayShowEvent = new DisplayShowEvent(this, player);
        Common.call(displayShowEvent);
        if (!displayShowEvent.isCancelled()) {
            player.sendTitle(title, subtitle, fadeInTime, stayTime, fadeOutTime);
        }
    }

    @Override
    public void show(@Nonnull Player player, long duration, @Nonnull TimeUnit unit) {
        Common.call(new DisplayHideEvent(this, player));
        player.sendTitle(title, subtitle, fadeInTime, (int) unit.toMillis(duration) / 50, fadeOutTime);
    }

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }

    /**
     * Gets the subtitle text.
     *
     * @return the subtitle text
     */
    @Nonnull
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the subtitle text.
     *
     * @param subtitleText the subtitle text
     */
    public void setSubtitle(@Nonnull String subtitleText) {
        this.subtitle = Text.color(subtitleText);
    }

    /**
     * Gets the fade in time.
     *
     * @return the fade in time
     */
    public int getFadeInTime() {
        return fadeInTime;
    }

    /**
     * Sets the fade in time.
     *
     * @param fadeInTime the fade in time
     */
    public void setFadeInTime(int fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    /**
     * Gets the stay time.
     *
     * @return the stay time
     */
    public int getStayTime() {
        return stayTime;
    }

    /**
     * Sets the stay time.
     *
     * @param stayTime the stay time
     */
    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    /**
     * Gets the fade out time.
     *
     * @return the fade out time
     */
    public int getFadeOutTime() {
        return fadeOutTime;
    }

    /**
     * Sets the fade out time.
     *
     * @param fadeOutTime the fade out time
     */
    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }

    @Nonnull
    @Override
    public List<Player> players() {
        throw new UnsupportedOperationException("This method has no operations.");
    }

    @Override
    public void hide(@Nonnull Player player) {
        throw new UnsupportedOperationException("This method has no operations.");
    }

    @Override
    public boolean isVisible() {
        throw new UnsupportedOperationException("This method has no operations.");
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException("This method has no operations.");
    }
}
