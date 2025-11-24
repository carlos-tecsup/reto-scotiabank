package com.scotiabank.challengue.application.util;

import com.scotiabank.challengue.application.enums.StatusEnum;
import org.mapstruct.Named;

public class StatusUtil {
    @Named("fromBooleanToStatus")
    public static String fromBooleanToStatus(Boolean isActive) {
        return Boolean.TRUE.equals(isActive) ? StatusEnum.ACTIVE.getValue() : StatusEnum.INACTIVE.getValue();
    }

    @Named("fromCodeToValue")
    public static String fromCodeToValue(String code) {
        if (StatusEnum.ACTIVE.getCod().equals(code)) {
            return StatusEnum.ACTIVE.getValue();
        } else if (StatusEnum.INACTIVE.getCod().equals(code)) {
            return StatusEnum.INACTIVE.getValue();
        }
        return code;
    }
}
