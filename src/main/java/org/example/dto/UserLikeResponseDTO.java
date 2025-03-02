package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserLikeResponseDTO {

    private String postTitle;
    private String postAuthorFirstName;
    private String postAuthorLastName;
    private LocalDateTime createdAt;
}
