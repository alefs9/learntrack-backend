package com.upc.learntrack.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    private Long id;

    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String name;

    @NotBlank(message = "El código es obligatorio")
    private String code;

    @NotNull(message = "El ID del profesor es obligatorio")
    private Long teacherId;

    private LocalDateTime createdAt;
}