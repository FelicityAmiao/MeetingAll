package com.group8.meetingall.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HighlightUtilTest {
  @Test
    public void highLightWords() {
        List<String> words = Arrays.asList("天气","冰淇淋","nice");
        String text = "今天天气真nice，天气针不戳想吃冰淇淋！";
        String result = HighlightUtil.highLightWords(text, words);

        assertEquals("今天<mark>天气</mark>真<mark>nice</mark>，<mark>天气</mark>针不戳想吃<mark>冰淇淋</mark>！",result);
    }
}
