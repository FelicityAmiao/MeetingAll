package com.group8.meetingall.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoomOptionVo {
    private String value;
    private String label;
    private List<Children> children;

    public RoomOptionVo(String value) {
        this.value = value;
        this.label = value;
    }
}
