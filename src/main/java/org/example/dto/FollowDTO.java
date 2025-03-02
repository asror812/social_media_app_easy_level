package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FollowDTO {

    @NotNull
    private UUID followerId;

    @NotNull
    private UUID followedId;
}
