package com.group8.meetingall.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighlightUtil {
    public static String highLightWords(String text, List<String> words) {
        for (String word : words) {
            Pattern p = Pattern.compile(word);
            Matcher m = p.matcher(text);
            if (m.find()) {
                text = m.replaceAll("<mark>" + word + "</mark>");
            }
        }
        return text;
    }
}
