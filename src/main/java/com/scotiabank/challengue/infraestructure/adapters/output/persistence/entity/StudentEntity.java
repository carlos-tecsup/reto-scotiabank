package com.scotiabank.challengue.infraestructure.adapters.output.persistence.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class StudentEntity {
    @Id
    private Long id;

    @Column("name")
    private String name;

    @Column("lastname")
    private String lastName;

    @Column("status")
    private String status;

    @Column("age")
    private Integer age;

}