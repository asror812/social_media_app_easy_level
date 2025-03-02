package org.example.service;

import org.example.dao.LikeDao;
import org.example.dao.PostDao;
import org.example.dao.UserDao;
import org.example.dto.LikeDTO;
import org.example.dto.PostLikeResponseDTO;
import org.example.dto.UserLikeResponseDTO;
import org.example.model.Like;
import org.example.model.Post;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private LikeDao likeDao;

    @Mock
    private UserDao userDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private LikeService likeService;


    @Test
    void mapToPostLikeResponseDTO() {
        LocalDate birthdate = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setFirstName("test firstname");
        user.setLastName("test lastname");
        user.setBirthDate(birthdate);

        Post post = new Post(UUID.randomUUID(), "test title", "test body",  user,  new ArrayList<>());

        Like like = new Like(UUID.randomUUID(), user, post, now);
        PostLikeResponseDTO responseDTO = PostLikeResponseDTO.builder()
                .firstName("test firstname")
                .lastName("test lastname")
                .createdAt(now)
                .build();


        assertEquals(likeService.mapToPostLikeResponseDTO(like), responseDTO);
    }

    @Test
    void mapToUserLikeResponseDTO() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate birthdate = LocalDate.now();

        User user = new User();
        user.setFirstName("test firstname");
        user.setLastName("test lastname");
        user.setBirthDate(birthdate);

        Post post = new Post(UUID.randomUUID(), "test title", "test body",  user,  new ArrayList<>());
        Like like = new Like(UUID.randomUUID(), user, post, now);

        UserLikeResponseDTO responseDTO = UserLikeResponseDTO.builder()
                .postAuthorFirstName("test firstname")
                .postAuthorLastName("test lastname")
                .postTitle("test title")
                .createdAt(now)
                .build();

        assertEquals(likeService.mapToUserLikeResponseDTO(like), responseDTO);
    }

    @Test
    void likePost() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        LikeDTO likeDTO = LikeDTO.builder()
                .userId(userId)
                .postId(postId)
                .build();

        User user = new User();
        Post post = new Post();

        Mockito.when(userDao.findUserById(userId)).thenReturn(Optional.of(user));
        Mockito.when(postDao.getPostById(postId)).thenReturn(Optional.of(post));

        likeService.likePost(likeDTO);

        Mockito.verify(likeDao, Mockito.times(1)).createLike(any(Like.class));
    }

    @Test
    void likePost_ShouldThrowIllegalArgumentException() {
        LikeDTO likeDTO = LikeDTO.builder()
                .userId(UUID.randomUUID())
                .postId(UUID.randomUUID())
                .build();

        assertThrows(IllegalArgumentException.class, () -> likeService.likePost(likeDTO));
    }

}