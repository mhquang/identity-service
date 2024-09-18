package com.identity_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.response.UserResponse;
import com.identity_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc // create mock request
@TestPropertySource("/test.properties")
public class UserControllerTest {

    // call to api
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // input and output data
    private UserCreationRequest request;
    private UserResponse response;
    private LocalDate dob;

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
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // given (input data): request, response
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String requestContent = objectMapper.writeValueAsString(request);

        // mock createUser service
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(response);

        // when: call createUser api
        // then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.id")
                        .value("cf0600f538b3"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception {
        // given: request, response
        request.setUsername("ww"); // username min 3 characters

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String requestContent = objectMapper.writeValueAsString(request);

        // mock createUser service
        // không cần gọi vì nó validate ở tầng controller
//        Mockito.when(userService.createUser(ArgumentMatchers.any()))
//                .thenReturn(response);

        // when: call createUser api
        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1002))
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Username must be at least 3 characters!"));
    }

}
