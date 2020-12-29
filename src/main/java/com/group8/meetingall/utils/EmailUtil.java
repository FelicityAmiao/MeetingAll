package com.group8.meetingall.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class EmailUtil {

    @Value("${email.hostName}")
    private static String hostName;
    @Value("${email.account}")
    private static String account;
    @Value("${email.authorizedCode}")
    private static String authorizedCode;

    public static boolean sendEmail(String address,String subject,String msg){
        boolean isSuccess = true;
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.163.com");
        email.setCharset("UTF-8");
        email.setSslSmtpPort("25");
        email.setAuthenticator(new DefaultAuthenticator("zhuzhuxia96@126.com", "HNCSGCOSUEDRVJGU"));
        try{
            email.setFrom("zhuzhuxia96@126.com");
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(address);
            email.setSentDate(new Date());
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

}
