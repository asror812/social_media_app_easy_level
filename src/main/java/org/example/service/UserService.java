package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.FollowDao;
import org.example.dao.UserDao;
import org.example.dto.FollowDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.Follow;
import org.example.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userdao;
    private final PostService postService;
    private final LikeService likeService;
    private final FollowDao followDao;

    private static final String FOLLOWED_NOT_FOUND_ERROR = "Followed not found with id: %s";
    private static final String FOLLOWER_NOT_FOUND_ERROR = "Follower not found with id: %s";
    private static final String USER_NOT_FOUND_ERROR = "User not found with id: %s";

    @Transactional
    public void createUser(UserCreateDTO createDTO) {
        User user = User.builder()
                .firstName(createDTO.getFirstName())
                .lastName(createDTO.getLastName())
                .birthDate(createDTO.getBirthDate())
                .build();

        userdao.createUser(user);
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userdao.findUserById(id).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR.formatted(id)));
        return mapToResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userdao.getAllUsers().stream().map(this::mapToResponseDTO).toList();
    }

    public UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .posts(user.getPosts().stream().map(postService::mapToUserPostResponseDTO).toList())
                .likes(user.getLikes().stream().map(likeService::mapToUserLikeResponseDTO).toList())
                .build();
    }


    @Transactional
    public void follow(FollowDTO followDTO) {
        UUID followedId = followDTO.getFollowedId();
        UUID followerId = followDTO.getFollowerId();

        User followed = userdao.findUserById(followedId).orElseThrow(() -> new IllegalArgumentException(FOLLOWED_NOT_FOUND_ERROR.formatted(followedId)));
        User follower = userdao.findUserById(followerId).orElseThrow(() -> new IllegalArgumentException(FOLLOWER_NOT_FOUND_ERROR.formatted(followerId)));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        followed.getFollowers().add(follow);
        follower.getFollowings().add(follow);
        followDao.createFollow(follow);
    }

    public Set<UserResponseDTO> getFollowings(UUID userId) {
        User user = userdao.findUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR.formatted(userId)));
        return user.getFollowings().stream().map(this::mapFollowingsToUserResponseDTO).collect(Collectors.toSet());
    }

    public UserResponseDTO mapFollowingsToUserResponseDTO(Follow follow) {

        User followed = follow.getFollowed();

        return mapToResponseDTO(followed);
    }

    public Set<UserResponseDTO> getFollowers(UUID userId) {
        User user = userdao.findUserById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_ERROR.formatted(userId)));
        return user.getFollowers().stream().map(this::mapFollowersToUserResponseDTO).collect(Collectors.toSet());
    }

    public UserResponseDTO mapFollowersToUserResponseDTO(Follow follow) {
        User follower = follow.getFollower();

        return mapToResponseDTO(follower);
    }
}
