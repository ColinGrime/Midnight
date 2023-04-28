package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.display.manager.DisplayType;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Title extends BaseDisplay {

    private String titleText;
    private String subtitleText;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;

    public Title(@Nonnull String titleText) {
        this.titleText = Text.color(titleText);
        this.subtitleText = "";
        this.fadeInTime = 20; // Default 1 second.
        this.stayTime = 60; // Default 3 seconds.
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
        return titleText;
    }

    @Override
    public void setText(@Nonnull String text) {
        this.titleText = Text.color(text);
    }

    @Override
    public void show(@Nonnull Player player) {
        player.sendTitle(titleText, subtitleText, fadeInTime, stayTime, fadeOutTime);
    }

    @Override
    public void show(@Nonnull Player player, long duration, @Nonnull TimeUnit unit) {
        player.sendTitle(titleText, subtitleText, fadeInTime, (int) unit.toMillis(duration) / 50, fadeOutTime);
    }

    /**
     * Gets the subtitle text.
     * @return the subtitle text
     */
    @Nonnull
    public String getSubtitle() {
        return subtitleText;
    }

    /**
     * Sets the subtitle text.
     * @param subtitleText the subtitle text
     */
    public void setSubtitle(@Nonnull String subtitleText) {
        this.subtitleText = Text.color(subtitleText);
    }

    /**
     * Gets the fade in time.
     * @return the fade in time
     */
    public int getFadeInTime() {
        return fadeInTime;
    }

    /**
     * Sets the fade in time.
     * @param fadeInTime the fade in time
     */
    public void setFadeInTime(int fadeInTime) {
        this.fadeInTime = fadeInTime;
    }

    /**
     * Gets the stay time.
     * @return the stay time
     */
    public int getStayTime() {
        return stayTime;
    }

    /**
     * Sets the stay time.
     * @param stayTime the stay time
     */
    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    /**
     * Gets the fade out time.
     * @return the fade out time
     */
    public int getFadeOutTime() {
        return fadeOutTime;
    }

    /**
     * Sets the fade out time.
     * @param fadeOutTime the fade out time
     */
    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime;
    }

    @Nonnull
    @Override
    public List<Player> players() {
        return List.of(); // no-op
    }

    @Override
    public void hide(@Nonnull Player player) {
        // no-op
    }

    @Override
    public boolean isVisible() {
        return false; // no-op
    }

    @Override
    public void setVisible(boolean visible) {
        // no-op
    }
}
