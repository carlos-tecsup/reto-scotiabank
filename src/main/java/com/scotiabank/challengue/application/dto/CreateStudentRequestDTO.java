package com.scotiabank.challengue.application.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateStudentRequestDTO extends BaseStudentDTO{
    private Boolean isActive;
}
