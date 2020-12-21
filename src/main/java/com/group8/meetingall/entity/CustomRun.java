package com.group8.meetingall.entity;

public class CustomRun {
    private String text;
    private boolean highlight;

    public CustomRun() {
    }

    public CustomRun(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
}
