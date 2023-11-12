package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.LoginRequest;
import com.squarecross.photoalbum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest dto) {
        log.info("UserController : {}", dto.getUserName());
        return ResponseEntity.ok().body(userService.login(dto.getUserName(),""));
    }
}
