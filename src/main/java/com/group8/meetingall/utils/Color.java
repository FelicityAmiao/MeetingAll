package com.group8.meetingall.utils;

/**
 * Joi Liu
 * This is used for word file style
 */
public enum Color {
    RED("FF0000"),
    BLACK("000000"),
    YELLOW("FFFF00"),
    GREEN("00FF00"),
    BLUE("0000FF");

    private String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
