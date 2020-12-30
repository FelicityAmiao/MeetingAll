package com.group8.meetingall.service;

import com.group8.meetingall.dto.RegisterDto;
import com.group8.meetingall.dto.UserDto;
import com.group8.meetingall.entity.AuthInformation;
import com.group8.meetingall.entity.User;
import com.group8.meetingall.repository.UserRepository;
import com.group8.meetingall.utils.DateTimeUtil;
import com.itmuch.lightsecurity.jwt.JwtOperator;
import com.itmuch.lightsecurity.jwt.UserOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final JwtOperator jwtOperator;
    private final UserOperator userOperator;
    @Autowired
    private UserRepository userRepository;

    public User getUser() {
        String username = userOperator.getUser().getUsername();
        return userRepository.findUserByUsername(username);
    }

    public String auth(UserDto userDto) {
        String username = userDto.getUsername();
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(userDto.getPassword())) {
            com.itmuch.lightsecurity.jwt.User jwtUser = com.itmuch.lightsecurity.jwt.User.builder()
                    .id(1)
                    .username(user.getUsername())
                    .build();
            return jwtOperator.generateToken(jwtUser);
        }
        return null;
    }

    public RegisterDto regist(String username, String password, Integer authCode){
        RegisterDto registerDto = new RegisterDto();
        registerDto.setSuccess(true);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        registerDto.setUser(user);
        List<AuthInformation> authInformations = userRepository.findAuthCode(username,authCode);
        if(authInformations == null || authInformations.size() == 0){
            registerDto.setSuccess(false);
            registerDto.setMsg("验证码错误");
            return registerDto;
        }
        if(DateTimeUtil.isBefore(authInformations.get(0).getDeadLine(),DateTimeUtil.getCurrentDateTime())){
            registerDto.setSuccess(false);
            registerDto.setMsg("验证码已超时，请重新获取。");
            return registerDto;
        }
        User registedUser = userRepository.upsertUser(user);
        registerDto.setUser(registedUser);
        AuthInformation authInformation = new AuthInformation();
        authInformation.setAuthCode(null);
        authInformation.setUsername(username);
        authInformation.setDeadLine(null);
        userRepository.upsertAuthCode(authInformation);
        return registerDto;
    }

    public boolean verifyUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        return user == null;
    }

    public boolean saveAuthCode(String username,Integer authCode){
        AuthInformation authInformation = new AuthInformation();
        authInformation.setUsername(username);
        authInformation.setAuthCode(authCode);
        authInformation.setDeadLine(DateTimeUtil.getOffsetTime(15));
        boolean isSuccess = userRepository.upsertAuthCode(authInformation);
        return isSuccess;
    }

}
