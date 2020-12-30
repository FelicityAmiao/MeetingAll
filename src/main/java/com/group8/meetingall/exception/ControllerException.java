package com.group8.meetingall.exception;

import com.itmuch.lightsecurity.exception.LightSecurityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerException {
  @ExceptionHandler(PasswordIsErrorException.class)
  public ResponseEntity handlePasswordIsErrorException(PasswordIsErrorException exception) {
    String str = "密码错误，请重新输入";
    return ResponseEntity.badRequest().body(str);
  }

  @ExceptionHandler(LightSecurityException.class)
  public ResponseEntity handlePasswordIsErrorException(LightSecurityException exception) {
    String str = "未经授权。";
    return ResponseEntity.badRequest().body(str);
  }

  @ExceptionHandler(UserHasExistedException.class)
  public ResponseEntity handleUserHasExistedException(UserHasExistedException exception) {
    String str = "邮箱已注册，请输入其他邮箱。";
    return ResponseEntity.badRequest().body(str);
  }

  @ExceptionHandler(UserNotExistedException.class)
  public ResponseEntity handlePackageNotExistedException(UserNotExistedException exception) {
    String str = "用户不存在，请重新输入。";
    return ResponseEntity.badRequest().body(str);
  }

}
