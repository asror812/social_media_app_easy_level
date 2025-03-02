package org.example.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.model.Like;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeDao {

    private final EntityManager entityManager;


    public void createLike(Like like) {
        entityManager.persist(like);
    }
}
