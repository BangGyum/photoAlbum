package com.squarecross.photoalbum.utils;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
    //토큰은 header.payload.signature (헤더.내용.서명) 으로 구성돼 있고, cliams는 내용 안에 있다.
    /**
     * 사용자 정보를 기반으로 토큰을 생성하여 반환 해주는 메서드
     *
     * @param userName  : 사용자 이름
     * @return String : 토큰
     */

    //userName 가져오기
    public static String getUserName(String token, String secretKey){
        log.info("getUserName//token:{}",token);
        log.info("getUserName//secretKey :{}",secretKey);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token); //여기가 문제다. 무슨 문제지?
                                //JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.
                                //로컬과 다르다..
                                //근데 로컬 토큰은 yml에서 같은것을 불러오니깐 일단 yml 문제는 아니다.

                                //값은 보통 eyJhbGciOiJIUzI1NiJ9.e30.99O810IDbBIemVzxUX4wV3KEi74a2D9IwEbUfYEAae4
                                //위와 같은 식이다.
        return claims
                .getBody().get("userName",String.class); //toString도 됨
    }

    //expired 확인
    public static boolean isExpired(String token, String secretKey){
        log.info("isExpired//token:{}",token);
        log.info("isExpired//secretKey :{}",secretKey);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);

        return claims.getBody()
                .getExpiration()
                .before(new Date());

    }

    //static이니깐 userService 만들어서 거기서 리턴해주자.
    public static String createJwt(String userName, String secretKey, Long expiredMs){ //secretKey는 토큰에 서명하는데 사용, userName은
                                                            // controller에서 check 할때 토큰의 유저네임을 꺼내서 표시
                                                            // jwt는 클레임이라고, 내가 원하는 정보를 저장하는 공간을 제공
                                                            // expired밀리세컨드
        Claims claims = Jwts.claims();
        claims.put("userName", userName);
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()  + expiredMs))        //언제까지
                .signWith(SignatureAlgorithm.HS256,secretKey) //해당 알고리즘으로 사인되었다.
                .compact();         //jw

    }
}
