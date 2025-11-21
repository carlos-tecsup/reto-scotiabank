package com.scotiabank.challengue.application.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;

public class StudentMapperTest {
    private StudentMapper studentMapper;
    @BeforeEach
    void setUp() {
        studentMapper = Mappers.getMapper(StudentMapper.class);
    }
}
