package org.example.service;

import lombok.*;
import org.example.dao.LikeDao;
import org.example.dao.PostDao;
import org.example.dao.UserDao;
import org.example.dto.LikeDTO;
import org.example.dto.PostLikeResponseDTO;
import org.example.dto.UserLikeResponseDTO;
import org.example.model.Like;
import org.example.model.Post;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserDao userDao;
    private final PostDao postDao;
    private final LikeDao likeDao;

    private static final String POST_NOT_FOUND_ERROR = "Post not found with id: %s";
    private static final String USER_NOT_FOUND_ERROR = "User not found with id: %s";

    public PostLikeResponseDTO mapToPostLikeResponseDTO(Like like){
        return PostLikeResponseDTO.builder()
                .firstName(like.getUser().getFirstName())
                .lastName(like.getUser().getLastName())
                .createdAt(like.getCreatedAt())
                .build();
    }

    public UserLikeResponseDTO mapToUserLikeResponseDTO(Like like){
        return UserLikeResponseDTO.builder()
                .postAuthorFirstName(like.getPost().getAuthor().getFirstName())
                .postAuthorLastName(like.getPost().getAuthor().getLastName())
                .postTitle(like.getPost().getTitle())
                .createdAt(like.getCreatedAt())
                .build();
    }

    @Transactional
    public void likePost(LikeDTO likeDTO) {
        UUID postId = likeDTO.getPostId();
        UUID userId = likeDTO.getUserId();

        User user = userDao.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR.formatted(userId.toString())));

        Post post = postDao.getPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException(POST_NOT_FOUND_ERROR.formatted(postId.toString())));

        Like like = new Like(null, user, post, LocalDateTime.now());
        post.getLikes().add(like);
        likeDao.createLike(like);
    }
}
