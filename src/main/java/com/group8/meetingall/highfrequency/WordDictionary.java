package com.group8.meetingall.highfrequency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordDictionary {

    private static WordDictionary wordDictionary;
    private DictionaryTrie dictionaryTrie;
    private static final String DICTIONARY_RELATIVE_PATH = "/dictionary/";
    private static final String DICTIONARY_ABSOLUTE_PATH = "src/main/resources/dictionary";
    private static final String MAIN_DICTIONARY = "/dictionary/main_dictionary.txt";
    private static String USER_DICT_SUFFIX = ".dict";
    public final Map<String, Double> wordFrequencyMap = new HashMap<>();
    private Double minFreq = Double.MAX_VALUE;
    private Double total = 0.0;


    private WordDictionary(){
        this.loadDictionary();
    }

    public static WordDictionary getInstance() {
        if (wordDictionary == null) {
            synchronized (WordDictionary.class) {
                if (wordDictionary == null) {
                    wordDictionary = new WordDictionary();
                    return wordDictionary;
                }
            }
        }
        return wordDictionary;
    }

    private void loadDictionary() {
        dictionaryTrie = new DictionaryTrie((char)0);
        List<String> dictionaries = getDictionaries();
        BufferedReader br = null;
        String line;
        try{
            for (String dictionary : dictionaries) {
                br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(dictionary), Charset.forName("UTF-8")));
                while ((line = br.readLine()) != null){
                    String[] word_freq = line.split("[\t ]+");
                    if(word_freq.length > 1){
                        String word = word_freq[0];
                        word = addWord(word);
                        double freq = Double.valueOf(word_freq[1]);
                        total += freq;
                        wordFrequencyMap.put(word,freq);
                    }
                }
                for (Map.Entry<String, Double> wordFrequency : wordFrequencyMap.entrySet()) {
                    wordFrequency.setValue(Math.log(wordFrequency.getValue()/total));
                    minFreq = Math.min(wordFrequency.getValue(),minFreq);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private List<String> getDictionaries(){
        List<String> dictionaries = new ArrayList<>();
        try {
            DirectoryStream<Path> userDictionaries = Files.newDirectoryStream(Paths.get(DICTIONARY_ABSOLUTE_PATH), String.format(Locale.getDefault(), "*%s", USER_DICT_SUFFIX));
            for (Path userDictionary : userDictionaries) {
                dictionaries.add(DICTIONARY_RELATIVE_PATH+userDictionary.getFileName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionaries.add(MAIN_DICTIONARY);
        return dictionaries;
    }

    private String addWord(String word) {
        if (null != word && !"".equals(word.trim())) {
            String key = word.trim().toLowerCase(Locale.getDefault());
            dictionaryTrie.fillTrie(key.toCharArray());
            return key;
        }
        return null;
    }

    public DictionaryTrie getDictionaryTrie() {
        return this.dictionaryTrie;
    }

    public boolean containsWord(String word){
        return wordFrequencyMap.containsKey(word);
    }

    public Double getFreq(String key) {
        return containsWord(key) ? wordFrequencyMap.get(key) : minFreq;
    }

}
