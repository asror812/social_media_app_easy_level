package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.FollowDTO;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserResponseDTO;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserCreateDTO createDTO){
        userService.createUser(createDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable(name = "id") UUID id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> follow(@Valid @RequestBody FollowDTO followDTO){
        userService.follow(followDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/following")
    public Set<UserResponseDTO> getFollowedUsers(@PathVariable UUID userId) {
        return userService.getFollowings(userId);
    }

    @GetMapping("/{userId}/followers")
    public Set<UserResponseDTO> getFollowers(@PathVariable UUID userId) {
        return userService.getFollowers(userId);
    }
}
