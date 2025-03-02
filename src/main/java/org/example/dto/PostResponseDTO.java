package org.example.dto;

import lombok.*;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Builder
public class PostResponseDTO {
    private String title;

    private String body;

    private String authorFirstName;

    private String authorLastName;

    private List<PostLikeResponseDTO> likes;
}
