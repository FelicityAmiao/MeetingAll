package com.group8.meetingall.utils;

import com.group8.meetingall.highfrequency.*;

import java.util.*;

public class DivideTextUtil {

    private static WordDictionary wordDictionary = WordDictionary.getInstance();
    private static FinalSeg finalSeg = FinalSeg.getInstance();

    public enum SegMode {
        SEARCH
    }

    public static List<String> splitText(String text){
        List<String> words = new ArrayList<>();
        int textLength = text.length();
        Map<Integer, List<Integer>> dag = createDAG(text);
        Map<Integer, Pair<Integer>> route = calc(text, dag);

        int x = 0;
        int y;
        String buf;
        StringBuilder sb = new StringBuilder();
        while (x < textLength) {
            y = route.get(x).key + 1;
            String lWord = text.substring(x, y);
            if (y - x == 1)
                sb.append(lWord);
            else {
                if (sb.length() > 0) {
                    buf = sb.toString();
                    sb = new StringBuilder();
                    if (buf.length() == 1) {
                        words.add(buf);
                    }
                    else {
                        if (wordDictionary.containsWord(buf)) {
                            words.add(buf);
                        }
                        else {
                            finalSeg.cut(buf, words);
                        }
                    }
                }
                words.add(lWord);
            }
            x = y;
        }
        buf = sb.toString();
        if (buf.length() > 0) {
            if (buf.length() == 1) {
                words.add(buf);
            }
            else {
                if (wordDictionary.containsWord(buf)) {
                    words.add(buf);
                }
                else {
                    finalSeg.cut(buf, words);
                }
            }

        }
        return words;
    }

    private static Map<Integer, List<Integer>> createDAG(String text) {
        Map<Integer, List<Integer>> dag = new HashMap<>();
        char[] charArray = text.toCharArray();
        int charArrayLength = charArray.length;
        DictionaryTrie dictionaryTrie = wordDictionary.getDictionaryTrie();
        int firstCharIndex = 0, nextCharIndex = 0;
        while(firstCharIndex < charArrayLength){
            Hit hit = dictionaryTrie.match(charArray,firstCharIndex,nextCharIndex-firstCharIndex+1);
            if(!dag.containsKey(firstCharIndex)){
                List<Integer> value = new ArrayList<>();
                value.add(firstCharIndex);
                dag.put(firstCharIndex,value);
            }
            if(hit.isMatch() || hit.isPrefix()){
                if(hit.isMatch() && firstCharIndex != nextCharIndex){
                    dag.get(firstCharIndex).add(nextCharIndex);
                }
                if(++nextCharIndex >= charArrayLength){
                    firstCharIndex++;
                    nextCharIndex = firstCharIndex;
                }
            }else{
                firstCharIndex++;
                nextCharIndex = firstCharIndex;
            }
        }
        return dag;
    }


    private static Map<Integer, Pair<Integer>> calc(String sentence, Map<Integer, List<Integer>> dag) {
        int N = sentence.length();
        HashMap<Integer, Pair<Integer>> route = new HashMap<Integer, Pair<Integer>>();
        route.put(N, new Pair<>(0, 0.0));
        for (int i = N - 1; i > -1; i--) {
            Pair<Integer> candidate = null;
            for (Integer x : dag.get(i)) {
                double freq = wordDictionary.getFreq(sentence.substring(i, x + 1)) + route.get(x + 1).freq;
                if (null == candidate) {
                    candidate = new Pair<>(x, freq);
                }
                else if (candidate.freq < freq) {
                    candidate.freq = freq;
                    candidate.key = x;
                }
            }
            route.put(i, candidate);
        }
        return route;
    }

}
