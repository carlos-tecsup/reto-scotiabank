package com.scotiabank.challengue.infraestructure.adapters.output.persistence.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRedisEntity implements Serializable {
    private Long id;
    private String name;
    private String lastName;
    private String status;
    private Integer age;
}
