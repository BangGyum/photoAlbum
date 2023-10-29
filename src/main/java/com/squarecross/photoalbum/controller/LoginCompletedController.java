package com.squarecross.photoalbum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.Authenticator;

@RestController
@RequestMapping("/")
public class LoginCompletedController {
    @PostMapping("check")
    public ResponseEntity<String> loginCheck(Authentication authentication) {

        return ResponseEntity.ok().body(authentication.getName() + "님의 당신은 로그인 된 사람이군요.");
    }
}
