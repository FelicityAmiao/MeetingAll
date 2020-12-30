package com.group8.meetingall.service;

import com.group8.meetingall.dto.UserDto;
import com.group8.meetingall.entity.User;
import com.group8.meetingall.repository.UserRepository;
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

    public User regist(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User registedUser = userRepository.upsertUser(user);
        return registedUser;
    }

    public boolean verifyUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        return user == null;
    }

}
