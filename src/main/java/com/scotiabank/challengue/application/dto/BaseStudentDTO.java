package com.scotiabank.challengue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Datos base de un estudiante")
public class BaseStudentDTO {

    @NotNull(message = "ID no debe ser nulo")
    @Min(value = 1, message = "ID debe ser mayor o igual que 1")
    @Schema(description = "Identificador único del estudiante", example = "1", required = true)
    private Long id;

    @NotBlank(message = "Nombre no puede estar en blanco o nulo")
    @Pattern(regexp = "^$|^[a-zA-Z]+$", message = "Nombre debe contener solo letras")
    @Schema(description = "Nombre del estudiante (solo letras)", example = "Juan", required = true)
    private String name;

    @NotBlank(message = "Apellido no puede estar en blanco o nulo")
    @Pattern(regexp = "^$|^[a-zA-Z]+$", message = "Apellido debe contener solo letras")
     @Schema(description = "Apellido del estudiante (solo letras)", example = "Perez", required = true)
    private String lastName;

    @Schema(description = "Estado del estudiante (activo/inactivo)", example = "activo")
    private String status;

    @NotNull(message = "Edad no puede ser nula")
    @Min(value = 1, message = "Edad debe ser mayor que 1")
    @Schema(description = "Edad del estudiante", example = "25", required = true)
    private Integer age;
}
