package com.group8.meetingall.dto.xfasr;

public class Text {
    int sn;
    String text;
    boolean deleted;
    boolean ls;
    @Override
    public String toString() {
        return "Text{" +
                ", ls=" + ls +
                ", sn=" + sn +
                ", text='" + text + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
