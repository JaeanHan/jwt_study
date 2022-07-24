package com.springboot.jwt_study.config;

import com.springboot.jwt_study.config.jwt.JwtAuthenticationFiler;
import com.springboot.jwt_study.config.jwt.JwtAuthorizationFilter;
import com.springboot.jwt_study.filter.MyFilter3;
import com.springboot.jwt_study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();

        // ↓ 시큐리티를 사용하지만 세션을 사용하지 않으니 모든 페이지로 접근이 가능하다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // ↑ STATELESS => session 사용x

        .and()
        .addFilter(corsFilter) //@CrossOrigin 사용시 인증이 필요한 요청은 거부됨
                               // 시큐리티 필터에 등록하면 인증필요한 요청도 허용용
        .formLogin().disable() //form 태그 로그인 x
        .httpBasic().disable() // 요청 때마다 id, pwd 들고가는 Basic 방식이 아닌 Bearer 방식을 사용하기 위함
        .addFilter(new JwtAuthenticationFiler(authenticationManager())) // extends UsernamePasswordAuthenticationFilter
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository)) // extends BasicAuthorizationFilter

        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest().permitAll()
        .and();
    }
}
