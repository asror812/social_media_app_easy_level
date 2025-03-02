package org.example.service;

import org.example.dao.FollowDao;
import org.example.dao.UserDao;
import org.example.dto.FollowDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.model.Follow;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PostService postService;

    @Mock
    private LikeService likeService;

    @Mock
    private FollowDao followDao;

    @InjectMocks
    private UserService userService;

    private static final String USER_NOT_FOUND_ERROR = "User not found with id: %s";
    private static final String FOLLOWED_NOT_FOUND_ERROR = "Followed not found with id: %s";


    @Test
    void createUser() {
        userService.createUser(UserCreateDTO.builder().build());
        Mockito.verify(userDao, Mockito.times(1)).createUser(Mockito.any(User.class));
    }

    @Test
    void getUserById_ShouldThrowIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserById(id));
        assertEquals(USER_NOT_FOUND_ERROR.formatted(id), exception.getMessage());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        UUID id = UUID.randomUUID();
        User user = User.builder().birthDate(LocalDate.now()).firstName("a").lastName("a").build();
        Mockito.when(userDao.findUserById(id)).thenReturn(Optional.of(user));

        UserResponseDTO responseDTO = userService.getUserById(id);
        assertEquals("a", responseDTO.getFirstName());
        assertEquals("a", responseDTO.getLastName());
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        User user1 = new User();
        user1.setFirstName("User1");
        user1.setLastName("Last1");

        User user2 = new User();
        user2.setFirstName("User2");
        user2.setLastName("Last2");

        Mockito.when(userDao.getAllUsers()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("User1", users.get(0).getFirstName());
        assertEquals("User2", users.get(1).getFirstName());

        Mockito.verify(userDao, Mockito.times(1)).getAllUsers();
        ;
    }

    @Test
    void follow() {
        FollowDTO followDTO = FollowDTO
                .builder()
                .followedId(UUID.randomUUID())
                .followedId(UUID.randomUUID())
                .build();
    }

    @Test
    void follow_ShouldReturnIllegalArgumentException() {
        UUID followedId = UUID.randomUUID();
        UUID followerId = UUID.randomUUID();

        FollowDTO followDTO = FollowDTO
                .builder()
                .followedId(followedId)
                .followerId(followerId)
                .build();
        Mockito.lenient().when(userDao.findUserById(followerId)).thenReturn(Optional.empty());
        Mockito.when(userDao.findUserById(followedId)).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.follow(followDTO));
        assertEquals(FOLLOWED_NOT_FOUND_ERROR.formatted(followedId), exception.getMessage());
    }

    @Test
    void getFollowings_ShouldReturnIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        Mockito.when(userDao.findUserById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getFollowings(id));


        assertEquals(USER_NOT_FOUND_ERROR.formatted(id), exception.getMessage());
    }

    @Test
    void getFollowings_ShouldReturnFollowings() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Follow follow = new Follow();
        follow.setFollower(user);
        follow.setFollowed(new User());

        user.getFollowings().add(follow);

        Mockito.when(userDao.findUserById(userId)).thenReturn(Optional.of(user));

        Set<UserResponseDTO> followings = userService.getFollowings(userId);

        assertNotNull(followings);
        assertEquals(1, followings.size());
    }

    @Test
    void getFollowers_ShouldReturnIllegalArgumentException() {
        UUID id = UUID.randomUUID();
        Mockito.when(userDao.findUserById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getFollowers(id));
        assertEquals(USER_NOT_FOUND_ERROR.formatted(id), exception.getMessage());
    }

    @Test
    void getFollowers_ShouldReturnFollowed() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Follow follow = new Follow();
        follow.setFollower(new User());
        follow.setFollowed(user);

        user.getFollowers().add(follow);

        Mockito.when(userDao.findUserById(userId)).thenReturn(Optional.of(user));

        Set<UserResponseDTO> followers = userService.getFollowers(userId);

        assertNotNull(followers);
        assertEquals(1, followers.size());
    }

    @Test
    public void testFollowUser() {
        UUID followerId = UUID.randomUUID();
        UUID followedId = UUID.randomUUID();
        FollowDTO followDTO = new FollowDTO(followerId, followedId);

        User follower = new User();
        follower.setId(followerId);

        User followed = new User();
        followed.setId(followedId);

        Mockito.when(userDao.findUserById(followerId)).thenReturn(Optional.of(follower));
        Mockito.when(userDao.findUserById(followedId)).thenReturn(Optional.of(followed));

        userService.follow(followDTO);

        Mockito.verify(followDao, Mockito.times(1)).createFollow(Mockito.any(Follow.class));
        assertEquals(1, follower.getFollowings().size());
        assertEquals(1, followed.getFollowers().size());
    }


}
