package com.group8.meetingall.utils;

import com.group8.meetingall.TestUtil;
import com.group8.meetingall.service.impl.HighFrequencyServiceImpl;
import com.group8.meetingall.vo.Keyword;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HighlightUtilTest {
    private HighFrequencyServiceImpl highFrequencyService = new HighFrequencyServiceImpl();

    @Test
    public void highLightWords() {
        List<String> words = Arrays.asList("天气", "冰淇淋", "nice");
        String text = "今天天气真nice，天气针不戳想吃冰淇淋！";
        String result = HighlightUtil.highLightWords(text, words);

        assertEquals("今天<mark>天气</mark>真<mark>nice</mark>，<mark>天气</mark>针不戳想吃<mark>冰淇淋</mark>！", result);
    }

    @Test
    public void highLightWords2() {
        URL url = this.getClass().getClassLoader().getResource("testData/Chinese_and_English.txt");
        String text = TestUtil.readStringFromFile(url.getPath());
        int count = (int) Math.ceil(text.length() * 0.01);
        StopWatch st = new StopWatch();
        st.start();
        List<Keyword> results = highFrequencyService.getHighFrequencyWords(text, count);
        List<String> words = results.stream().map(Keyword::getWord).collect(Collectors.toList());
        String highlightText = HighlightUtil.highLightWords(text, words);
        st.stop();

        assertTrue(highlightText.length() > text.length());
        System.out.println("cost "+st.getTotalTimeSeconds()+"s.");
    }
}
