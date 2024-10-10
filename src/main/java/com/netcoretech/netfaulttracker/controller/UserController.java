package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.dto.UserRegistrationDto;
import com.netcoretech.netfaulttracker.entity.User;
import com.netcoretech.netfaulttracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";  // login.html 파일을 반환
    }

    @GetMapping("/register")
    public String register() {
        return "register";  // register.html 파일을 반환
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username, @RequestParam String password) {
        if (userService.authenticate(username, password)) {
            return "redirect:/";  // 로그인 성공 시 메인 페이지로 리다이렉트
        } else {
            return "redirect:/login?error=true";  // 실패 시 로그인 페이지로 리다이렉트
        }
    }

    @PostMapping("/api/v1/auth/register")
    public String registerProcess(UserRegistrationDto userDto) {
        try {
            userService.createUser(userDto);
            return "redirect:/login?registerSuccess=true";  // 회원가입 후 로그인 페이지로 이동
        } catch (Exception e) {
            return "redirect:/register?error=true";  // 회원가입 실패 시 다시 회원가입 페이지로 리다이렉트
        }
    }


    @GetMapping("/api/v1/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/v1/users/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/users/{id}")
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto userDto) {
        return userService.getUserById(id)
                .map(existingUser -> {
                    User updatedUser = userService.updateUser(userDto);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/v1/users/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
