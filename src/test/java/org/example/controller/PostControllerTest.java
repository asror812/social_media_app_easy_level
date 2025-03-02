package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostResponseDTO;
import org.example.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createPost_ShouldReturnStatusCreated() throws Exception {
        PostCreateDTO createDTO = PostCreateDTO.builder()
                .title("Test title")
                .body("Test body")
                .authorId(UUID.randomUUID())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .content(objectMapper.writeValueAsString(createDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(postService).createPost(any(PostCreateDTO.class));
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() throws Exception {
        List<PostResponseDTO> posts = List.of(new PostResponseDTO());
        when(postService.getAllPosts()).thenReturn(posts);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

        verify(postService).getAllPosts();
    }

    @Test
    void getPostById_ShouldReturnPost() throws Exception {
        UUID postId = UUID.randomUUID();
        PostResponseDTO responseDTO = new PostResponseDTO();
        when(postService.getPostById(postId)).thenReturn(responseDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).getPostById(postId);
    }
}

