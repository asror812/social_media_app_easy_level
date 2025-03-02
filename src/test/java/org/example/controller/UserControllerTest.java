package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.FollowDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void createUser_ShouldReturnStatusCreated() throws Exception {

        objectMapper.registerModule(new JavaTimeModule());
        UserCreateDTO createDTO = UserCreateDTO.builder()
                .firstName("asror")
                .lastName("r")
                .birthDate(LocalDate.now())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .content(objectMapper.writeValueAsString(createDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void getAllPosts_ShouldReturnUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(userService.getUserById(userId)).thenReturn(new UserResponseDTO());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getAllPosts_ShouldReturnListOfUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void follow_ShouldReturnStatusOk() throws Exception {
        FollowDTO followDTO = FollowDTO.builder()
                .followedId(UUID.randomUUID())
                .followerId(UUID.randomUUID())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(followDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}