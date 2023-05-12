package me.colingrimes.channels.channel.misc.filter.profanity;

import me.colingrimes.channels.config.Filters;
import me.colingrimes.channels.message.ChannelMessage;

import javax.annotation.Nonnull;

/**
 * An aggressive profanity filter that filters out messages containing banned words
 * using the similarity distance method to aggressively detect variations and misspellings.
 */
public class AggressiveProfanityFilter extends ProfanityFilter {

    private static final AdvancedProfanityFilter advancedProfanityFilter = new AdvancedProfanityFilter();

    @Override
    public boolean filterProfanity(@Nonnull ChannelMessage<?> message) {
        String content = message.toText().toLowerCase();
        String[] words = content.split("\\s+");

        for (String word : words) {
            for (String bannedWord : Filters.PROFANITY_LIST.get()) {
                if (calculateSimilarityDistance(word, bannedWord) <= Filters.PROFANITY_MAX_ALLOWED_SIMILARITY_DISTANCE.get()) {
                    return true;
                }
            }
        }

        return advancedProfanityFilter.filterProfanity(message);
    }

    /**
     * Calculates the similarity distance between two strings.
     * @param s1 the first string
     * @param s2 the second string
     * @return the similarity distance between the two strings
     */
    private int calculateSimilarityDistance(@Nonnull String s1, @Nonnull String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];

        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                curr[j] = Math.min(Math.min(curr[j - 1], prev[j]), prev[j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1));
            }

            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[s2.length()];
    }
}
