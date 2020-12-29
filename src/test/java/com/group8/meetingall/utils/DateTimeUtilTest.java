package com.group8.meetingall.utils;


import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

    @Test
    public void test(){
        String offsetTime = DateTimeUtil.getOffsetTime(15);
        System.out.println(offsetTime);
    }

}