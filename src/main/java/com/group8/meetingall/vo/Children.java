package com.group8.meetingall.vo;

import lombok.Data;

@Data
public class Children {
    private String value;
    private String label;

    public Children(String value) {
        this.value = value;
        this.label = value;
    }
}
