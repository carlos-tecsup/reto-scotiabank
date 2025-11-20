package com.scotiabank.challengue.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO  {

    @NotNull(message = "ID no debe ser nulo")
    @Min(value = 1, message = "ID debe ser mayor o igual que 1")
    private Long id;

    @NotBlank(message = "Nombre no puede estar en blanco o nulo")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Nombre debe contener solo letras")
    private String name;

    @NotBlank(message = "Apellido no puede estar en blanco o nulo")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Apellido debe contener solo letras")
    private String lastName;

    private Boolean isActive;

    @NotNull(message = "Edad no puede ser nula")
    @Min(value = 1, message = "Edad debe ser mayor que 1")
    private Integer age;


}
