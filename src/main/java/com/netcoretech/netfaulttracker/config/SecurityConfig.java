package com.netcoretech.netfaulttracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/issues/**") // 특정 경로에 대해 CSRF 보호 비활성화
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/issues/**").permitAll() // 이슈 API 접근을 권한 없이 허용
                        .anyRequest().permitAll() // 그 외 모든 요청 허용
                )
                .formLogin(form -> form.disable()) // 로그인 폼 비활성화
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
