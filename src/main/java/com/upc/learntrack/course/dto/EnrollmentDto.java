package com.upc.learntrack.course.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentDto {
    private Long id;

    @NotBlank(message = "El correo del estudiante es obligatorio")
    @Email(message = "El formato del correo es inválido")
    private String studentEmail;

    private Long groupId;
    private LocalDateTime enrolledAt;
}