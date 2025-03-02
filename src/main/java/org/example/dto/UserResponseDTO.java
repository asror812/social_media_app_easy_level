package org.example.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private List<UserPostResponseDTO> posts;

    private List<UserLikeResponseDTO> likes;
}
