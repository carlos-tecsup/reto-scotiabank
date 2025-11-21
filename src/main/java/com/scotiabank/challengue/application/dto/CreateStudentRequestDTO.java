package com.scotiabank.challengue.application.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class CreateStudentRequestDTO extends BaseStudentDTO{
    private Boolean isActive;
}
