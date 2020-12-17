package com.group8.meetingall.service.impl;

import com.group8.meetingall.TestUtil;
import com.group8.meetingall.vo.Keyword;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HighFrequencyServiceImplTest {

    private HighFrequencyServiceImpl highFrequencyService = new HighFrequencyServiceImpl();

    @Test
    void getHighFrequencyWords() {
        URL url = this.getClass().getClassLoader().getResource("testData/Chinese_and_English.txt");
        String text = TestUtil.readStringFromFile(url.getPath());
        int count = (int) Math.ceil(text.length() * 0.01);
        StopWatch st = new StopWatch();
        st.start();
        List<Keyword> results = highFrequencyService.getHighFrequencyWords(text, count);
        st.stop();

        assertEquals(count, results.size());
        assertEquals("疾病", results.get(0).getWord());
        assertEquals("脑出血", results.get(1).getWord());
        assertEquals("血压", results.get(2).getWord());
        assertEquals("老年人", results.get(3).getWord());
        System.out.println("cost " + st.getTotalTimeSeconds() + "s.");
    }
}
