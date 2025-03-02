package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.example.model.Post;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostDao {
    private final EntityManager entityManager;

    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PostDao.class);
    private final static String POST_NOT_FOUND = "Post not found with id: {}";

    public void createPost(Post post) {
        entityManager.persist(post);
    }

    public List<Post> getAllPosts() {
        return entityManager.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

    public Optional<Post> getPostById(UUID id) {
        return Optional.ofNullable(entityManager.find(Post.class, id));
    }

    public List<Post> getPostsByAuthorId(UUID id) {
            return entityManager
                    .createQuery("SELECT p FROM Post p WHERE p.author.id = :id", Post.class).setParameter("id", id).getResultList();

    }

    public Post getPostByTitle(String title) {
        return entityManager.createQuery("SELECT p FROM Post p WHERE p.title = :title", Post.class).setParameter("title", title).getSingleResult();
    }
}
