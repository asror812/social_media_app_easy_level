package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LikeDTO {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID postId;
}
