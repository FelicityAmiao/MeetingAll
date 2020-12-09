package com.group8.meetingall.highfrequency;

import com.group8.meetingall.utils.DivideTextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TermFrequency {

    public static Map<String, Double> getTermFrequency(String text) {
        if(text == null || "".equalsIgnoreCase(text.trim())){
            return new HashMap<>();
        }
        List<String> words = DivideTextUtil.splitText(text);
        return calculateTF(words);
    }

    private static Map<String,Double> calculateTF(List<String> words){
        Map<String,Double> tfMap=new HashMap<>();
        Map<String,Integer> frequencyMap=new HashMap<>();
        Set<String> stopWords = StopWords.getInstance();
        int wordCount = 0;
        for (String word : words) {
            if(!stopWords.contains(word) && word.length()>1){
                wordCount++;
                if(frequencyMap.containsKey(word)){
                    frequencyMap.put(word,frequencyMap.get(word)+1);
                }else{
                    frequencyMap.put(word,1);
                }
            }
        }
        for (String word : frequencyMap.keySet()) {
            tfMap.put(word,frequencyMap.get(word)*0.1/wordCount);
        }
        return tfMap;
    }

}
