package com.shaluo.snapbite.controller;

import com.shaluo.snapbite.dto.RegisterRequest;
import com.shaluo.snapbite.model.User;
import com.shaluo.snapbite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }
}
