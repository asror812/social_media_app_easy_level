package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LikeDTO;
import org.example.service.LikeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void likePost() throws Exception{
        LikeDTO likeDTO = LikeDTO.builder()
                .postId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/posts/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(likeDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}