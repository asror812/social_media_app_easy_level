package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.NoResultException;
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
class UserDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<User> query;

    @InjectMocks
    private UserDao userDao;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
    }

    @Test
    void createUser() {
        userDao.createUser(user);
        verify(entityManager, times(1)).persist(user);
    }

    @Test
    void findUserById_UserExists() {
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("id", userId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(user);

        Optional<User> result = userDao.findUserById(userId);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void findUserById_UserNotFound() {
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.setParameter("id", userId)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException());

        Optional<User> result = userDao.findUserById(userId);
        assertFalse(result.isPresent());
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(user);
        when(entityManager.createQuery(anyString(), eq(User.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        List<User> result = userDao.getAllUsers();
        assertEquals(users, result);
    }
}
