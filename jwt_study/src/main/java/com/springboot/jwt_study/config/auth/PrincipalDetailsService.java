package com.springboot.jwt_study.config.auth;

import com.springboot.jwt_study.model.User;
import com.springboot.jwt_study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//http://localhost:8080/login 요청시 동작
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService 호출");
        return new PrincipalDetails(userRepository.findByUsername(username));
    }
}
