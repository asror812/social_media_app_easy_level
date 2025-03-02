package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.model.Like;
import org.example.model.Post;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LikeDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Post> typedQuery;

    @InjectMocks
    private LikeDao likeDao;

    @Test
    void createLike() {
        Like like = new Like(UUID.randomUUID(), new User(), new Post(), LocalDateTime.now());

        likeDao.createLike(like);
        Mockito.verify(entityManager, Mockito.times(1)).persist(like);
    }
}