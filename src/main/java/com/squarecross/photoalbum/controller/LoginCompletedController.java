package com.squarecross.photoalbum.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoginCompletedController {
    @PostMapping("check")
    public ResponseEntity<String> loginCheck() {
        return ResponseEntity.ok().body("당신은 로그인 된 사람이군요.");
    }
}
