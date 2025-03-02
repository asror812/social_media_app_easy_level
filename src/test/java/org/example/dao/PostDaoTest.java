package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;
   import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Post> typedQuery;

    @InjectMocks
    private PostDao postDao;

    private Post post;
    private UUID postId;

    @BeforeEach
    void setUp() {
        postId = UUID.randomUUID();
        post = new Post();
        post.setId(postId);
        post.setTitle("Test Title");
    }

    @Test
    void createPost() {
        postDao.createPost(post);
        Mockito.verify(entityManager, Mockito.times(1)).persist(post);
    }

    @Test
    void getAllPosts() {
        List<Post> posts = Arrays.asList(post, new Post());
        Mockito.when(entityManager.createQuery("SELECT p FROM Post p", Post.class)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(posts);

        List<Post> result = postDao.getAllPosts();
        assertEquals(2, result.size());
    }

    @Test
    void getPostById() {
        Mockito.when(entityManager.find(Post.class, postId)).thenReturn(post);
        Post result = postDao.getPostById(postId).orElseThrow();
        assertEquals(postId, result.getId());
    }

    @Test
    void getPostsByAuthorId() {
        UUID authorId = UUID.randomUUID();
        List<Post> posts = List.of(post);

        Mockito.when(entityManager.createQuery("SELECT p FROM Post p WHERE p.author.id = :id", Post.class)).thenReturn(typedQuery);
        Mockito.when(typedQuery.setParameter("id", authorId)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(posts);

        List<Post> result = postDao.getPostsByAuthorId(authorId);
        assertFalse(result.isEmpty());
    }

    @Test
    void getPostByTitle() {
        Mockito.when(entityManager.createQuery("SELECT p FROM Post p WHERE p.title = :title", Post.class)).thenReturn(typedQuery);
        Mockito.when(typedQuery.setParameter("title", "Test Title")).thenReturn(typedQuery);
        Mockito.when(typedQuery.getSingleResult()).thenReturn(post);

        Post result = postDao.getPostByTitle("Test Title");
        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
    }
}