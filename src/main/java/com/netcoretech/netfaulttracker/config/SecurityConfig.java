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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/index.html", "/login", "/register", "/css/**", "/js/**", "/api/**", "/error")
                        .permitAll()  // 인증 없이 접근 허용
                        .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 로그인 페이지 설정
                        .defaultSuccessUrl("/index.html", true)  // 로그인 성공 후 이동할 페이지
                        .failureUrl("/login?error=true")  // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")  // 로그아웃 성공 후 이동할 페이지
                        .invalidateHttpSession(true)  // 세션 무효화
                        .clearAuthentication(true)  // 인증 정보 삭제
                        .deleteCookies("JSESSIONID")  // 쿠키 삭제
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // API 요청에 대해 CSRF 예외 처리
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")  // 권한이 없을 때 이동할 페이지
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?invalid-session")  // 세션 만료 시 이동할 페이지
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
