package com.storecera.ai.gemini;

import com.storecera.util.L;
import com.storecera.util.Util;

import java.util.Arrays;
import java.util.List;

public class FollowUpDetector {

    private static final String[] FOLLOW_UP_PATTERNS = {
            "last \\d+ (day|week|month|year)s?",
            "past \\d+ (day|week|month|year)s?",
            "previous \\d+ (day|week|month|year)s?",
            "next \\d+ (day|week|month|year)s?",
            "\\d+ (day|week|month|year)s? ago",
            "within \\d+ (day|week|month|year)s?",
            "in the last \\d+ (day|week|month|year)s?",
            "in the past \\d+ (day|week|month|year)s?",
            "in the next \\d+ (day|week|month|year)s?",
            "in \\d+ (day|week|month|year)s time?"
    };

    private static final List<String> FOLLOW_UP_KEYWORDS = Arrays.asList(
            "what about", "how about", "and yesterday", "that one", "also",
            "again", "too", "before", "previous", "from last time", "you mentioned",
            "the same", "another one", "last one","next", "again, please", "add on", "continue",
            "how about", "what of", "another", "and also", "by the way", "previously",
            "furthermore", "so far", "after that", "can you check", "last time", "last week",
            "last month", "this week", "this month", "this year", "the day before yesterday",
            "yesterday", "tomorrow", "next month", "in the past", "last [day of the week]"

//            "what about", "how about", "and yesterday", "that one", "also",
//            "again", "too", "before", "previous", "from last time", "you mentioned",
//            "the same", "another one", "last one","next", "again, please", "add on", "continue",
//            "how about", "what of", "another", "and also", "by the way", "previously",
//            "furthermore", "so far", "after that", "can you check", "last time", "last week", "last one week",
//            "last month", "this week", "this month", "this year", "the day before yesterday",
//            "yesterday", "last one month", "next month", "in the past", "two days ago", "last [day of the week]"
    );

    public static boolean isPromptAFollowUp(String prompt) {
        return isKeywordBasedFollowUp(prompt);
    }

    /**
     * Checks if user input likely refers to a previous message by using common continuation phrases.
     */
    public static boolean isKeywordBasedFollowUp(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        int count = 0;
        String lower = Util.safeLower(input);

        for (String phrase : FOLLOW_UP_KEYWORDS) {
            if (lower.contains(phrase) && count > 1) {
                return true;
            }
            count++;
        }

        for (String pattern : FOLLOW_UP_PATTERNS) {
            if (lower.matches(".*\\b" + pattern + "\\b.*") && count > 1) {
                L.msg("It is a followup prompt");
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the input is very short or lacks key intent words, suggesting it's a continuation.
     */
    public static boolean isShortOrContextDependent(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        String[] words = input.trim().split("\\s+");
        if (words.length <= 5) {
            // Very short phrases might be follow-ups
            String[] intentWords = { "add", "get", "check", "list", "buy", "spend", "show", "calculate" };
            for (String word : intentWords) {
                if (input.toLowerCase().contains(word)) return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Extracts time range from prompt, e.g., "last 2 weeks" → (2, "week")
     */
//    public static Pair<Integer, String> extractTimeRange(String userInput) {
//        Pattern pattern = Pattern.compile("(\\d+)\\s*(day|week|month|year)s?");
//        Matcher matcher = pattern.matcher(userInput.toLowerCase());
//
//        if (matcher.find()) {
//            int value = Integer.parseInt(matcher.group(1));
//            String unit = matcher.group(2);
//            return new Pair<>(value, unit);
//        }
//
//        return null;
//    }

    /**
     * Combines all checks to determine if input is likely a follow-up.
     */
    public static boolean isFollowUp(String input) {
        return isKeywordBasedFollowUp(input) || isShortOrContextDependent(input);
    }
}