package com.group8.meetingall.utils;

import org.junit.jupiter.api.Test;


class EmailUtilTest {

    @Test
    public void test(){
        EmailUtil.sendEmail("153011490@qq.com","测试","hello world");
    }

}