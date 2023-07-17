package com.nasco.circuitbreaker.controller;

import com.nasco.circuitbreaker.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {


    @GetMapping
    public List<User> getUsers() {
        User user1 = new User(1, "Ahmed", "Employee");
        User user2 = new User(2, "Kareem", "Employee");
        return Arrays.asList(user1, user2);
    }

}

