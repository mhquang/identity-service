package com.identity_service.controller;

import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.request.UserUpdateRequest;
import com.identity_service.dto.response.APIResponse;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import com.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @GetMapping
    public APIResponse<List<UserResponse>> getUsers() {
        return APIResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
    }

    @PostMapping
    public APIResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Controller: create user");
        return APIResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping("/{userId}")
    public APIResponse<UserResponse> getUser(@PathVariable String userId) {
        return APIResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
    }

    @GetMapping("/myInfo")
    public APIResponse<UserResponse> getMyInfo() {
        return APIResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }

    @PutMapping("/{userId}")
    public APIResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        return APIResponse.<UserResponse>builder().result(userService.updateUser(userId, request)).build();
    }

    @DeleteMapping("/{userId}")
    public APIResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return APIResponse.<String>builder().result("User has been deleted!").build();
    }
}
