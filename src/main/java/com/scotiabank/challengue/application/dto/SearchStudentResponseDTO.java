package com.scotiabank.challengue.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response con la lista de estudiantes encontrados")
public class SearchStudentResponseDTO {
    
    @Schema(description = "Lista de estudiantes que cumplen los criterios de búsqueda")
    private List<BaseStudentDTO> students;
}
