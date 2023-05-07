package me.colingrimes.midnight.channel.filter.profanity;

import me.colingrimes.midnight.message.implementation.ChannelMessage;
import me.colingrimes.plugin.config.Filters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * An advanced profanity filter that filters out messages containing banned words,
 * considering variations with leetspeak and special characters.
 */
public class AdvancedProfanityFilter extends ProfanityFilter {

    private final List<Pattern> profanityPatterns = new ArrayList<>();

    public AdvancedProfanityFilter() {
        for (String profanityWord : Filters.PROFANITY_LIST.get()) {
            String pattern = createLeetSpeakPattern(profanityWord);
            profanityPatterns.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        }
    }

    @Override
    public boolean filter(@Nonnull ChannelMessage<?> message) {
        String content = message.toText();
        return profanityPatterns.stream().noneMatch(pattern -> pattern.matcher(content).find());
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
                .replaceAll("s", "[sS5$]")
                .replaceAll("t", "[tT7]")
                .replaceAll("u", "[uUvV]")
                .replaceAll("v", "[vVuU]")
                .replaceAll("w", "[wW]")
                .replaceAll("x", "[xX]")
                .replaceAll("y", "[yY]")
                .replaceAll("z", "[zZ2]");
    }
}
