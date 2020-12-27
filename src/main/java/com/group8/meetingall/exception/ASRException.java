package com.group8.meetingall.exception;

public class ASRException extends Exception {
    private String requestId;
    private String errorCode;

    public ASRException(String message) {
        this(message, "", "");
    }

    public ASRException(String message, String requestId, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.errorCode = errorCode;
    }

}
