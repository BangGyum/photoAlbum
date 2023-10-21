package com.squarecross.photoalbum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable() //토큰 인증으로 할거기에
                .csrf().disable() //역시 토큰 사용을 위해
                .cors().and()
                .authorizeRequests()
                .antMatchers("/login").permitAll() //언제나 가능
                .antMatchers(HttpMethod.POST, "/**").authenticated() //모든것
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하면 사용
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)
                //받은 토큰으로 풀기 위해서 secretkey가 필요하다
                //id와 pw로 이미 인증했기에 토큰을 준 것이다. 때문에 JwtToken이 먼저오고 그다음 u.p.a
                .build();
    }
}
