package com.group8.meetingall.vo;

public class Keyword {

    public String word;
    public Double frequency;

    public Keyword() {
    }

    public Keyword(String word, Double frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }
}
