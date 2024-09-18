package com.identity_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.identity_service.dto.request.UserCreationRequest;
import com.identity_service.dto.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;


@SpringBootTest
@Slf4j
@AutoConfigureMockMvc // create mock request
@Testcontainers
public class UserControllerIntegrationTest {
    // init container
    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    // connect to mysql container to test
    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl_auto", () -> "update");
    }

    // call to api
    @Autowired
    private MockMvc mockMvc;

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

        // when: call createUser api
        // then
        var response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1000))
                .andExpect(MockMvcResultMatchers.jsonPath("result.username")
                        .value("walter"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName")
                        .value("Walter"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.lastName")
                        .value("White"));

        log.info("Result: {}", response.andReturn().getResponse().getContentAsString());
    }


}
