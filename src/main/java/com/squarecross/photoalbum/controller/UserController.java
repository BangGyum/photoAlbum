package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
//@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login() {

        return ResponseEntity.ok().body(userService.login("aa","aa"));
    }
}
