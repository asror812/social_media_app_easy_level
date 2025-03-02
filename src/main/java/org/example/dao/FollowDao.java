package org.example.dao;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.model.Follow;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowDao {

    private final EntityManager entityManager;

    public void createFollow(Follow follow){
        entityManager.persist(follow);
    }
}
