package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Value("${jwt.secret}")
    private String secretKey;

    private Long expiredMs = 1000 * 60l * 60l;
    public String login(String userName, String password) {
        log.info("UserService//userName:{}",userName);
        log.info("secretKey :{}",secretKey);
        //일단 jwt만 구현
        //인증 과정 생략
        return JwtUtil.createJwt(userName,secretKey,expiredMs);
    }
}
