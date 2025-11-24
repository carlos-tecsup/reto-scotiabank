package com.scotiabank.challengue.infraestructure.config.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotaciones de documentación Swagger para los endpoints de estudiantes.
 */
public class StudentApiDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @RouterOperations({
            @RouterOperation(
                    path = "/api/students/create",
                    method = RequestMethod.POST,
                    beanClass = com.scotiabank.challengue.infraestructure.adapters.input.rest.StudentHandler.class,
                    beanMethod = "registerStudent",
                    operation = @Operation(
                            operationId = "registerStudent",
                            summary = "Registrar nuevo estudiante",
                            description = "Crea un nuevo estudiante en el sistema. Ejemplo: {\"id\": 16, \"name\": \"Andrea\", \"lastName\": \"Silva\", \"age\": 27, \"isActive\": true}",
                            tags = {"Students"},
                            requestBody = @RequestBody(
                                    description = "Datos del estudiante a crear",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = com.scotiabank.challengue.application.dto.CreateStudentRequestDTO.class,
                                                    example = "{\"id\": 16, \"name\": \"Andrea\", \"lastName\": \"Silva\", \"age\": 27, \"isActive\": true}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Estudiante creado exitosamente"
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Datos de entrada inválidos - Errores de validación",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            example = "{\"code\":\"validation_error\",\"errors\":[{\"field\":\"name\",\"message\":\"Nombre no puede estar en blanco o nulo\"}]}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "El estudiante ya existe - ID duplicado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            example = "{\"code\":\"business_error\",\"field\":\"id\",\"message\":\"Ya hay un estudiante registrado con id 16\"}"
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/students/search",
                    method = RequestMethod.GET,
                    beanClass = com.scotiabank.challengue.infraestructure.adapters.input.rest.StudentHandler.class,
                    beanMethod = "searchStudents",
                    operation = @Operation(
                            operationId = "searchStudents",
                            summary = "Buscar estudiantes",
                            description = "Busca estudiantes según filtros. Ejemplos: {} (todos), {\"isActive\": true} (solo activos), {\"isActive\": false} (solo inactivos)",
                            tags = {"Students"},
                            requestBody = @RequestBody(
                                    description = "Filtros de búsqueda (opcional). Dejar vacío {} para obtener todos los estudiantes",
                                    required = false,
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = com.scotiabank.challengue.application.dto.SearchStudentsRequestDTO.class,
                                                    example = "{\"isActive\": true}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de estudiantes encontrados",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            implementation = com.scotiabank.challengue.application.dto.SearchStudentResponseDTO.class,
                                                            example = "{\"students\":[{\"id\":1,\"name\":\"Juan\",\"lastName\":\"Perez\",\"status\":\"activo\",\"age\":26}]}"
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public @interface StudentRouterDoc {
    }
}
