package com.identity_service.service;

import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.request.UserUpdateRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import com.identity_service.enums.ErrorCode;
import com.identity_service.enums.Role;
import com.identity_service.exception.AppException;
import com.identity_service.mapper.UserMapper;
import com.identity_service.repository.RoleRepository;
import com.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        try {
            user = userRepository.save(user);
        } catch(DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        return userMapper.toUserResponse(user);
    }


    // @PreAuthorize - check before call method
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    // @PostAuthorize - check after call method
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User updateUser = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(updateUser, request);
        updateUser.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        updateUser.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(updateUser));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public UserResponse getMyInfo() {
        var authenticationObj = SecurityContextHolder.getContext().getAuthentication();
        String name = authenticationObj.getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
}
