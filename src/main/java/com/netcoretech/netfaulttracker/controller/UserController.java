package com.netcoretech.netfaulttracker.controller;

import com.netcoretech.netfaulttracker.dto.UserRegistrationDto;
import com.netcoretech.netfaulttracker.entity.User;
import com.netcoretech.netfaulttracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @GetMapping("/api/v1/users")
    @ResponseBody
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/api/v1/users/{id}")
    @ResponseBody
    @Operation(summary = "Get a user by ID", description = "Retrieves a user by their ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/v1/users")
    @ResponseBody
    @Operation(summary = "Create a new user", description = "Creates a new user")
    public User createUser(@RequestBody UserRegistrationDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping("/api/v1/users/{id}")
    @ResponseBody
    @Operation(summary = "Update a user", description = "Updates an existing user")
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
    @Operation(summary = "Delete a user", description = "Deletes a user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    userService.deleteUser(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}