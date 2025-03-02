package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostCreateDTO {

    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String body;

    @NotNull
    private UUID authorId;

}
