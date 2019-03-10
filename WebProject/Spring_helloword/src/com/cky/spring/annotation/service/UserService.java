package com.cky.spring.annotation.service;

import com.cky.spring.annotation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void add(){
        System.out.println("userService add...");
        userRepository.save();
    }
}
