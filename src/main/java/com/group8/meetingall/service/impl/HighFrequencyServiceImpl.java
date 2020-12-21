package com.group8.meetingall.service.impl;

import com.group8.meetingall.highfrequency.InverseDocumentFrequency;
import com.group8.meetingall.highfrequency.TermFrequency;
import com.group8.meetingall.service.HighFrequencyService;
import com.group8.meetingall.utils.HighlightUtil;
import com.group8.meetingall.vo.Keyword;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class HighFrequencyServiceImpl implements HighFrequencyService {
    @Value("${keyword.percent}")
    private double percent;

    @Override
    public List<Keyword> getHighFrequencyWords(String text, Integer count) {
        if (count == null) {
            count = 1;
        }
        List<Keyword> keywordList = new ArrayList<>();
        Map<String, Double> idfMap = InverseDocumentFrequency.getInstance();
        Map<String, Double> tfMap = TermFrequency.getTermFrequency(text);
        tfMap.keySet().forEach(word -> {
            if (idfMap.containsKey(word)) {
                keywordList.add(new Keyword(word, tfMap.get(word) * idfMap.get(word)));
            } else {
                keywordList.add(new Keyword(word, tfMap.get(word) * InverseDocumentFrequency.idfMedian));
            }
        });
        if (keywordList.size() > count) {
            return keywordList.stream().sorted(Comparator.comparing(Keyword::getFrequency).reversed()).limit(count).collect(Collectors.toList());
        }
        return keywordList.stream().sorted(Comparator.comparing(Keyword::getFrequency).reversed()).collect(Collectors.toList());
    }

    @Override
    public XWPFDocument generateHighlightWordFile(String text) {
        if (nonNull(text) && text.length() > 0) {
            int count = (int) Math.ceil(text.length() * percent);
            List<Keyword> highFrequencyWords = getHighFrequencyWords(text, count);
            List<String> words = highFrequencyWords.stream().map(Keyword::getWord).collect(Collectors.toList());
            try {
                return HighlightUtil.createWord(text, words);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
