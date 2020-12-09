package com.group8.meetingall.utils;

public class SliceIdGenerator {

    private static final String INIT_STR = "aaaaaaaaa`";
    private int length = 0;
    private char[] ch;

    public SliceIdGenerator() {
        this.length = INIT_STR.length();
        this.ch = INIT_STR.toCharArray();
    }

    public String getNextSliceId() {
        for (int i = 0, j = length - 1; i < length && j >= 0; i++) {
            if (ch[j] != 'z') {
                ch[j]++;
                break;
            } else {
                ch[j] = 'a';
                j--;
                continue;
            }
        }
        return new String(ch);
    }

}