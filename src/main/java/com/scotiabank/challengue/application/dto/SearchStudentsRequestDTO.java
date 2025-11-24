package com.scotiabank.challengue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para buscar estudiantes con filtros opcionales")
public class SearchStudentsRequestDTO {
    
    @Schema(description = "Filtrar por estado activo/inactivo. Si es null, retorna todos", example = "true", required = false)
    private Boolean isActive;
}
