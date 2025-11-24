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
 * Anotaciones de documentación Swagger para los endpoints de estudiantes con Redis.
 */
public class RedisStudentApiDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @RouterOperations({
            @RouterOperation(
                    path = "/api/redis/students/create",
                    method = RequestMethod.POST,
                    beanClass = com.scotiabank.challengue.infraestructure.adapters.input.rest.StudentRedisHandler.class,
                    beanMethod = "registerStudent",
                    operation = @Operation(
                            operationId = "registerStudentRedis",
                            summary = "Registrar nuevo estudiante en Redis",
                            description = "Crea un nuevo estudiante en Redis con TTL de 30 minutos",
                            tags = {"Redis Students"},
                            requestBody = @RequestBody(
                                    description = "Datos del estudiante a crear en Redis",
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(
                                                    implementation = com.scotiabank.challengue.application.dto.CreateStudentRequestDTO.class,
                                                    example = "{\"id\": 200, \"name\": \"Maria\", \"lastName\": \"Garcia\", \"age\": 30, \"isActive\": true}"
                                            )
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "204",
                                            description = "Estudiante creado exitosamente en Redis"
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
                                            description = "El estudiante ya existe en Redis - ID duplicado",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            example = "{\"code\":\"business_error\",\"field\":\"id\",\"message\":\"Ya hay un estudiante registrado con id 200\"}"
                                                    )
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/redis/students/search",
                    method = RequestMethod.GET,
                    beanClass = com.scotiabank.challengue.infraestructure.adapters.input.rest.StudentRedisHandler.class,
                    beanMethod = "searchStudents",
                    operation = @Operation(
                            operationId = "searchStudentsRedis",
                            summary = "Buscar estudiantes en Redis",
                            description = "Busca estudiantes almacenados en Redis según filtros. Resultados ordenados por ID descendente. Ejemplos: {} (todos), {\"isActive\": true} (solo activos), {\"isActive\": false} (solo inactivos)",
                            tags = {"Redis Students"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de estudiantes encontrados en Redis (ordenados por ID descendente)",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(
                                                            implementation = com.scotiabank.challengue.application.dto.SearchStudentResponseDTO.class,
                                                            example = "{\"students\":[{\"id\":10,\"name\":\"Gabriela\",\"lastName\":\"Rojas\",\"status\":\"activo\",\"age\":20}]}"
                                                    )
                                            )
                                    )
                            }
                    )
            )
    })
    public @interface RedisStudentRouterDoc {
    }
}
