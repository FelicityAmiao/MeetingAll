package com.group8.meetingall.service;

import com.group8.meetingall.entity.User;
import com.group8.meetingall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User login(String username,String password){
        User user = new User();
        user.setUsername(username);
        List<User> users = userRepository.findUserByUsername(username);
        if(users == null || users.size() == 0){
            return user;
        }
        if(users.get(0).getPassword().equals(password)){
            user = users.get(0);
        }
        return user;
    }

    public User regist(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User registedUser = userRepository.upsertUser(user);
        return registedUser;
    }

    public boolean verifyUsername(String username){
        List<User> users = userRepository.findUserByUsername(username);
        return users == null || users.size() == 0;
    }

}
