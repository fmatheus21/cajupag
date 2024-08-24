package com.fmatheus.app.controller.util;

import com.fmatheus.app.controller.enumerable.BalanceTypeEnum;

public class AppUtil {

    AppUtil() {
        throw new IllegalStateException(getClass().getName());
    }

    public static BalanceTypeEnum determineBalanceType(String mcc) {
        return switch (mcc) {
            case "5411", "5412" -> BalanceTypeEnum.FOOD;
            case "5811", "5812" -> BalanceTypeEnum.MEAL;
            default -> BalanceTypeEnum.CASH;
        };
    }

}
