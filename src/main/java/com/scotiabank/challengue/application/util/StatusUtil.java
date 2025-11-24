package com.scotiabank.challengue.application.util;

import com.scotiabank.challengue.application.enums.StatusEnum;
import org.mapstruct.Named;

public class StatusUtil {
    @Named("fromBooleanToStatus")
    public static String fromBooleanToStatus(Boolean isActive) {
        return Boolean.TRUE.equals(isActive) ? StatusEnum.ACTIVE.getDesc() : StatusEnum.INACTIVE.getDesc();
    }
}
