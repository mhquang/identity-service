package com.identity_service.service;

import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.entity.User;
import com.identity_service.exception.AppException;
import com.identity_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    // input and output data
    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

    private User user;

    // initialize data before run test
    @BeforeEach
    void initData() {
        dob = LocalDate.of(1958, 9, 7);

        request = UserCreationRequest.builder()
                .username("walter")
                .firstName("Walter")
                .lastName("White")
                .password("12345678")
                .dob(dob)
                .build();

        response = UserResponse.builder()
                .id("cf0600f538b3")
                .username("walter")
                .firstName("Walter")
                .lastName("White")
                .dob(dob)
                .build();

        user = User.builder()
                .id("cf0600f538b3")
                .username("walter")
                .firstName("Walter")
                .lastName("White")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // when
        var response = userService.createUser(request);

        // then
        assertThat(response.getId()).isEqualTo("cf0600f538b3");
        assertThat(response.getUsername()).isEqualTo("walter");
    }

    @Test
    void createUser_userExisted_fail() {
        // given
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // when
        var exception = assertThrows(AppException.class, () -> userService.createUser(request));

        // then
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }

    @Test
    @WithMockUser(username = "walter")
    void getMyInfo_valid_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        assertThat(response.getUsername()).isEqualTo("walter");
        assertThat(response.getId()).isEqualTo("cf0600f538b3");
    }

    @Test
    @WithMockUser(username = "walter")
    void getMyInfo_userNotFound_error() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());

        assertThat(exception.getErrorCode().getCode()).isEqualTo(1001);
    }
}
