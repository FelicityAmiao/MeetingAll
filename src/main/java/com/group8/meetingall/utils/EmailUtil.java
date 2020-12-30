package com.group8.meetingall.utils;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmailUtil {

    private static String hostName;
    private static String account;
    private static String authorizedCode;
    private static String port;

    public static boolean sendEmail(String address,String subject,String msg){
        boolean isSuccess = true;
        SimpleEmail email = new SimpleEmail();
        email.setHostName(hostName);
        email.setCharset("UTF-8");
        email.setSslSmtpPort(port);
        email.setSSLOnConnect(true);
        email.setAuthenticator(new DefaultAuthenticator(account, authorizedCode));
        try{
            email.setFrom(account);
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

    @Value("${email.hostName}")
    public void setHostName(String hostName) {
        EmailUtil.hostName = hostName;
    }

    @Value("${email.account}")
    public void setAccount(String account) {
        EmailUtil.account = account;
    }

    @Value("${email.authorizedCode}")
    public void setAuthorizedCode(String authorizedCode) {
        EmailUtil.authorizedCode = authorizedCode;
    }

    @Value("${email.port}")
    public void setPort(String port) {
        EmailUtil.port = port;
    }
}
