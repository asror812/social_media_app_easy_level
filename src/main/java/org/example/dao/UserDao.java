package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final EntityManager entityManager;
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(UserDao.class);
    private final static String USER_NOT_FOUND = "User not found with id: {}";

    public void createUser(User user) {
        entityManager.persist(user);
    }

    public Optional<User> findUserById(UUID id) {
        try {
            return Optional
                    .ofNullable(
                            entityManager
                                    .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                                    .setParameter("id", id)
                                    .getSingleResult());
        } catch (NoResultException e) {
            logger.info(USER_NOT_FOUND, id);
            return Optional.empty();
        }
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
}
