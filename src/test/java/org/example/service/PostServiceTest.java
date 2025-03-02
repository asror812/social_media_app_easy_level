package org.example.service;

import org.example.dao.PostDao;
import org.example.dao.UserDao;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostResponseDTO;
import org.example.dto.UserPostResponseDTO;
import org.example.model.Post;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostDao postDao;

    @Mock
    private UserDao userDao;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private PostService postService;

    private UUID postId;
    private UUID authorId;
    private User author;
    private Post post;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        authorId = UUID.randomUUID();

        author = User.builder()
                .id(authorId)
                .firstName("John")
                .lastName("Doe")
                .build();

        post = Post.builder()
                .id(postId)
                .title("Test Title")
                .body("Test Body")
                .author(author)
                .likes(List.of()) // No likes initially
                .build();
    }

    // ✅ Test createPost method (Successful case)
    @Test
    void createPost_ShouldCreatePostSuccessfully() {
        PostCreateDTO postCreateDTO = PostCreateDTO.builder()
                .authorId(authorId)
                .title("New Post")
                .body("New Body")
                .build();

        when(userDao.findUserById(authorId)).thenReturn(Optional.of(author));

        postService.createPost(postCreateDTO);

        verify(postDao, times(1)).createPost(any(Post.class));
    }

    // ❌ Test createPost method (User not found)
    @Test
    void createPost_ShouldThrowException_WhenUserNotFound() {
        PostCreateDTO postCreateDTO = PostCreateDTO.builder()
                .authorId(authorId)
                .title("New Post")
                .body("New Body")
                .build();

        when(userDao.findUserById(authorId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> postService.createPost(postCreateDTO));
        assertEquals("User not found with id: " + authorId, exception.getMessage());

        verify(postDao, never()).createPost(any(Post.class));
    }

    // ✅ Test getAllPosts method
    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        when(postDao.getAllPosts()).thenReturn(List.of(post));

        List<PostResponseDTO> posts = postService.getAllPosts();

        assertEquals(1, posts.size());
        assertEquals("Test Title", posts.get(0).getTitle());
    }

    // ✅ Test getPostById method (Successful case)
    @Test
    void getPostById_ShouldReturnPost() {
        when(postDao.getPostById(postId)).thenReturn(Optional.of(post));

        PostResponseDTO result = postService.getPostById(postId);

        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Body", result.getBody());
        assertEquals("John", result.getAuthorFirstName());
    }

    // ❌ Test getPostById method (Post not found)
    @Test
    void getPostById_ShouldThrowException_WhenPostNotFound() {
        when(postDao.getPostById(postId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> postService.getPostById(postId));
        assertEquals("Post not found with id: " + postId, exception.getMessage());
    }

    // ✅ Test mapToUserPostResponseDTO method
    @Test
    void mapToUserPostResponseDTO_ShouldMapCorrectly() {
        UserPostResponseDTO response = postService.mapToUserPostResponseDTO(post);

        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Body", response.getBody());
        assertEquals("John", response.getAuthorFirstName());
        assertEquals("Doe", response.getAuthorLastName());
    }
}
