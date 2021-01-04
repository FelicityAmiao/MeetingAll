package com.group8.meetingall.service;

import com.group8.meetingall.highfrequency.InverseDocumentFrequency;
import com.group8.meetingall.highfrequency.StopWords;
import com.group8.meetingall.highfrequency.TermFrequency;
import com.group8.meetingall.repository.TranslateResultRepository;
import com.group8.meetingall.utils.HighlightUtil;
import com.group8.meetingall.vo.Keyword;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class HighFrequencyService {
    @Autowired
    TranslateResultRepository translateResultRepository;
    @Value("${keyword.percent}")
    private double percent;
    @Value("${filePath.report}")
    private String reportPath;

    public List<Keyword> getHighFrequencyWords(String text, Integer count) {
        if (count == null) {
            count = 1;
        }
        List<Keyword> keywordList = new ArrayList<>();
        Map<String, Double> idfMap = InverseDocumentFrequency.getInstance();
        Map<String, Double> tfMap = TermFrequency.getTermFrequency(text);
        Set<String> stopWords = StopWords.getInstance();
        tfMap.keySet().forEach(word -> {
            boolean isStopWord = false;
            for (int i = 0; i < word.length(); i++) {
                if (stopWords.contains(word.substring(i, i + 1))) {
                    isStopWord = true;
                    break;
                }
            }
            if (!isStopWord) {
                if (idfMap.containsKey(word)) {
                    keywordList.add(new Keyword(word, tfMap.get(word) * idfMap.get(word)));
                } else {
                    keywordList.add(new Keyword(word, tfMap.get(word) * InverseDocumentFrequency.idfMedian));
                }
            }
        });
        if (keywordList.size() > count) {
            return keywordList.stream().sorted(Comparator.comparing(Keyword::getFrequency).reversed()).limit(count).collect(Collectors.toList());
        }
        return keywordList.stream().sorted(Comparator.comparing(Keyword::getFrequency).reversed()).collect(Collectors.toList());
    }

    public void generateHighlightWordFile(String uuid, String fileName, String subject) {
        String reportContent = translateResultRepository.getFileContent(uuid);
        if (nonNull(reportContent) && reportContent.length() > 0) {
            int count = (int) Math.ceil(reportContent.length() * percent);
            List<Keyword> highFrequencyWords = getHighFrequencyWords(reportContent, count);
            List<String> words = highFrequencyWords.stream().map(Keyword::getWord).collect(Collectors.toList());
            try {
                XWPFDocument word = HighlightUtil.createWord(reportContent, words, subject);
                String wholeFilePath = reportPath + fileName;
                File file = new File(reportPath);
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }
                File targetFile = new File(wholeFilePath);
                FileOutputStream out = new FileOutputStream(targetFile);
                word.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
