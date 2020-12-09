package com.group8.meetingall.highfrequency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class StopWords {

    private final static String STOP_WORDS_PATH = "/dictionary/stop_words.txt";
    private static Set<String> stopWordsSet;

    private StopWords(){

    }

    public static Set<String> getInstance(){
        if(stopWordsSet == null){
            stopWordsSet = getStopWords();
        }
        return stopWordsSet;
    }

    private static Set<String> getStopWords(){
        Set<String> stopWordsSet = new HashSet<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(StopWords.class.getResourceAsStream(STOP_WORDS_PATH)));
        try{
            String word;
            while((word = bufferedReader.readLine()) != null){
                stopWordsSet.add(word.trim());
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stopWordsSet;
    }
}
