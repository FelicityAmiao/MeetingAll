package com.group8.meetingall.highfrequency;

public class Pair<K> {
    public K key;
    public Double freq = 0.0;

    public Pair(K key, double freq) {
	this.key = key;
	this.freq = freq;
    }
}
