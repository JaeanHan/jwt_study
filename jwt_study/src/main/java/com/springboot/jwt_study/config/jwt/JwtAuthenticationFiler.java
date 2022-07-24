package com.springboot.jwt_study.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.jwt_study.config.auth.PrincipalDetails;
import com.springboot.jwt_study.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에 UsernamePasswordAuthenticationFilter 가 있음
// /login 요청을해서 username, password 전송하면 (post)
// 해당 필터가 동작을 함.

// 그런데 SecurityFilter 에서 formLogin disable 해서 작동을 안함 지금
@RequiredArgsConstructor
public class JwtAuthenticationFiler extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("attemptAuthentication 함수 실행");

        // 1. username, password 받아서
        // 2. 정상인지 로그인 시도를 해보는 것
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailsService 호출 (loadByUsername)
        // 3. PrincipalDetails를 세션에 담고 (세션에 담지 않으면 권한 관리가 안됨)
        // 4. JWT 토큰을 만들어서 응답해주면 됨
        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService 내부 loadUserByUsername 메소드 실행
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // authentication을 이용해서 토큰을 만들었다.
            // 토큰이 만들어졌다 -> 디비에 로그인 정보가 존재한다.
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인 정보 : " + principalDetails.getUser().getUsername()); // 로그인 확인용
            System.out.println("로그인 정보 : " + principalDetails.getUser().getRoles()); // 로그인 확인용

            // Authentication 객체 반환시 해당 객체를 세션에 저장
            // 권한관리를 spring security가 대신 해주기 때문에 세션에 저장
            // 이때 jwt을 사용하면 세션을 만들 이유가 없지만 권한처리 때문에 세션 생성
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 해당 메소드 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 호출 : 인증완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA 방식은 아니고 Hash
        String jwtToken = JWT.create()
                .withSubject("tokenName")
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * 60 * 10)))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("jaean"));

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
