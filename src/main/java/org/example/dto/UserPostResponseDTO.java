package org.example.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Builder
public class UserPostResponseDTO {
    private String title;

    private String body;

    private String authorFirstName;

    private String authorLastName;

}
