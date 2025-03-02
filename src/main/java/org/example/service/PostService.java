package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.PostDao;
import org.example.dao.UserDao;
import org.example.dto.PostCreateDTO;
import org.example.dto.PostResponseDTO;
import org.example.dto.UserPostResponseDTO;
import org.example.model.Post;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDao postDao;
    private final UserDao userDao;
    private final LikeService likeService;

    private static final String POST_NOT_FOUND_ERROR = "Post not found with id: %s";
    private static final String USER_NOT_FOUND_ERROR = "User not found with id: %s";

    @Transactional
    public void createPost(PostCreateDTO post) {
        User user = userDao.findUserById(post.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR.formatted(post.getAuthorId())));
        Post newPost = Post
                .builder()
                .title(post.getTitle())
                .body(post.getBody())
                .author(user)
                .build();

        postDao.createPost(newPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        return postDao.getAllPosts().stream().map(this::mapToResponseDTO).toList();
    }

    public PostResponseDTO getPostById(UUID id) {
        return mapToResponseDTO(postDao.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException(POST_NOT_FOUND_ERROR.formatted(id))));
    }

    public PostResponseDTO mapToResponseDTO(Post post) {
        return PostResponseDTO.builder()
                .authorFirstName(post.getAuthor().getFirstName())
                .authorLastName(post.getAuthor().getLastName())
                .title(post.getTitle())
                .body(post.getBody())
                .likes(post.getLikes().stream().map(likeService::mapToPostLikeResponseDTO).toList())
                .build();
    }

    public UserPostResponseDTO mapToUserPostResponseDTO(Post post) {
        return UserPostResponseDTO.builder()
                .title(post.getTitle())
                .authorFirstName(post.getAuthor().getFirstName())
                .authorLastName(post.getAuthor().getLastName())
                .body(post.getBody())
                .build();
    }


}
