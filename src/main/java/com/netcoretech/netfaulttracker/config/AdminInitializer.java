package com.netcoretech.netfaulttracker.config;

import com.netcoretech.netfaulttracker.entity.User;
import com.netcoretech.netfaulttracker.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${netfaulttracker.admin.username}")
    private String adminUsername;

    @Value("${netfaulttracker.admin.password}")
    private String adminPassword;

    @Value("${netfaulttracker.admin.email}")
    private String adminEmail;

    public AdminInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userService.existsByUsername(adminUsername)) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(adminPassword); // 여기서는 평문 비밀번호 사용
            adminUser.setEmail(adminEmail);
            adminUser.setRole("ADMIN");
            userService.createUser(adminUser); // UserService에서 비밀번호 인코딩
            System.out.println("관리자 계정을 생성했습니다.");
        }
    }
}