package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PostLikeResponseDTO {
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
}
