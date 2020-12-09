package com.group8.meetingall.highfrequency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseDocumentFrequency {

    private final static String INVERSE_DOCUMENT_FREQUENCY_PATH = "/dictionary/idf_dict.txt";
    private static Map<String,Double> inverseDocumentFrequency;
    public static Double idfMedian = 0.0d;

    private InverseDocumentFrequency(){

    }

    public static Map<String,Double> getInstance(){
        if(inverseDocumentFrequency == null){
            inverseDocumentFrequency = getInverseDocumentFrequency();
        }
        return inverseDocumentFrequency;
    }

    private static Map<String,Double> getInverseDocumentFrequency(){
        Map<String,Double> inverseDocumentFrequencyMap = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(InverseDocumentFrequency.class.getResourceAsStream(INVERSE_DOCUMENT_FREQUENCY_PATH)));
        try{
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] word_idf = line.trim().split(" ");
                inverseDocumentFrequencyMap.put(word_idf[0],Double.valueOf(word_idf[1]));
            }
            bufferedReader.close();
            List<Double> idfList=new ArrayList<>(inverseDocumentFrequencyMap.values());
            Collections.sort(idfList);
            idfMedian=idfList.get(idfList.size()/2);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inverseDocumentFrequencyMap;
    }

}
