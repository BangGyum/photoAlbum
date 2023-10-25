package com.squarecross.photoalbum.configuration;

import com.squarecross.photoalbum.service.UserService;
import com.squarecross.photoalbum.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter { //요청매번 토큰이 있는지 췍, d

    private final UserService userService;

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //우리가 다 막아놓고, 여기가 검문체크

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization:{}",authorization);

        //권한 부여하기 전에 return , 그래도 필터체인이 가야함
        if(authorization == null || !authorization.startsWith("Bearer ")){
            log.error("authorization 이 없거나 잘못 보냈습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        //Token 꺼내기
        String token = authorization.split(" ")[1];

        //Token Expired 여부
        if(JwtUtil.isExpired(token,secretKey)){
            log.error("token이 만료 됐습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        //username을 token에서 꺼내기
        String userName = "";

        //권한부여, db에 roll을 저장해뒀으면 저기다가 박아넣을 수 있음, 지금은 하드코딩
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));

        //Detail 추가
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); //autentication set하고
        filterChain.doFilter(request,response); //filterChain에다가 request를 넘겨주면 request에 인증이 됐다고 사인이 된다.
    }
}
