package com.group8.meetingall.utils;

import com.group8.meetingall.TestUtil;
import com.group8.meetingall.entity.CustomRun;
import com.group8.meetingall.service.HighFrequencyService;
import com.group8.meetingall.vo.Keyword;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HighlightUtilTest {
    private HighFrequencyService highFrequencyService = new HighFrequencyService();

    @Test
    public void highLightWords() {
        List<String> words = Arrays.asList("天气", "冰淇淋", "nice", "吃");
        String text = "今天天气真nice，天气针不戳想吃冰淇淋！";
        String result = HighlightUtil.preProcessText(text, words);

        assertEquals("今天<天气<真<nice<，<天气<针不戳想<吃<<冰淇淋<！", result);
    }

    @Test
    public void generateText() {
        List<String> words = Arrays.asList("abc", "de");
        String text = "<abc<<de<fghijklmnopqrstuvwxyz<abc<<de<fghijklmnopqrstuvwxyz";
        List<CustomRun> customRuns = HighlightUtil.generateCustomRuns(text, words);

        assertEquals(6, customRuns.size());
        assertTrue(customRuns.get(0).isHighlight());
        assertEquals("abc", customRuns.get(0).getText());

    }

    @Test
    public void test() throws Exception{
        URL url = this.getClass().getClassLoader().getResource("testData/Chinese_and_English.txt");
        String text = TestUtil.readStringFromFile(url.getPath());
        int count = (int) Math.ceil(text.length() * 0.01);
        List<Keyword> results = highFrequencyService.getHighFrequencyWords(text, count);
        List<String> words = results.stream().map(Keyword::getWord).collect(Collectors.toList());

        HighlightUtil.createWord(text, words);
    }
}
