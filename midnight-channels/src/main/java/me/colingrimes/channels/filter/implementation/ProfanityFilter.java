package me.colingrimes.channels.filter.implementation;

import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.config.Filters;
import me.colingrimes.channels.config.Messages;
import me.colingrimes.channels.filter.ChatFilterType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A chat filter that filters out messages containing banned words,
 * considering variations with leetspeak and special characters.
 */
public class ProfanityFilter extends BaseFilter {

    private final List<Pattern> profanityPatterns = new ArrayList<>();

    @Override
    boolean filterMessage(@Nonnull String text, @Nonnull Chatter chatter) {
        if (profanityPatterns.isEmpty()) {
            for (String profanityWord : Filters.PROFANITY_LIST.getContent()) {
                String pattern = createLeetSpeakPattern(profanityWord);
                profanityPatterns.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
            }
        }

        if (profanityPatterns.stream().anyMatch(pattern -> pattern.matcher(text).find())) {
            chatter.send(Messages.PROFANITY_WARNING);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public ChatFilterType getType() {
        return ChatFilterType.PROFANITY;
    }

    /**
     * Creates a regex pattern that matches the given word and its leetspeak variations.
     * @param word the word for which to create a pattern
     * @return the regex pattern string
     */
    private String createLeetSpeakPattern(@Nonnull String word) {
        return word
                .replaceAll("a", "[aA4@]")
                .replaceAll("b", "[bB8]")
                .replaceAll("c", "[cC]")
                .replaceAll("d", "[dD]")
                .replaceAll("e", "[eE3]")
                .replaceAll("f", "[fF]")
                .replaceAll("g", "[gG9]")
                .replaceAll("h", "[hH]")
                .replaceAll("i", "[iIlL1!]")
                .replaceAll("j", "[jJ]")
                .replaceAll("k", "[kK]")
                .replaceAll("l", "[lL1!]")
                .replaceAll("m", "[mM]")
                .replaceAll("n", "[nN]")
                .replaceAll("o", "[oO0]")
                .replaceAll("p", "[pP]")
                .replaceAll("q", "[qQ]")
                .replaceAll("r", "[rR]")
                .replaceAll("s", "[sS5\\$]")
                .replaceAll("t", "[tT7]")
                .replaceAll("u", "[uUvV]")
                .replaceAll("v", "[vVuU]")
                .replaceAll("w", "[wW]")
                .replaceAll("x", "[xX]")
                .replaceAll("y", "[yY]")
                .replaceAll("z", "[zZ2]");
    }
}
