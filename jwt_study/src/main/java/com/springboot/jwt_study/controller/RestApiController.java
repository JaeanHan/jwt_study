package com.springboot.jwt_study.controller;

import com.springboot.jwt_study.model.User;
import com.springboot.jwt_study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

// @CrossOrigin 인증이 필요한 요청은 모두 거부됨
@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping("home")
    public String home() {
        return "<h1>Home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입 완료!";
    }

    // user, manager, admin 권한 접근 가능
    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    // manager, admin 권한 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    // admin 권한 접근가능
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
