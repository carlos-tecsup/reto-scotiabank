package com.scotiabank.challengue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear un nuevo estudiante")
public class CreateStudentRequestDTO extends BaseStudentDTO{
    
    @Schema(description = "Indica si el estudiante está activo o inactivo", example = "true", required = true)
    private Boolean isActive;
}
