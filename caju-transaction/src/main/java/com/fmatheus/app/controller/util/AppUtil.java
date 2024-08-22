package com.fmatheus.app.controller.util;

import com.fmatheus.app.controller.enumerable.BalanceTypeEnum;

public class AppUtil {

    AppUtil() {
        throw new IllegalStateException(getClass().getName());
    }

    public static String determineBalanceType(String mcc) {
        return switch (mcc) {
            case "5411", "5412" -> BalanceTypeEnum.FOOD.name();
            case "5811", "5812" -> BalanceTypeEnum.MEAL.name();
            default -> BalanceTypeEnum.CASH.name();
        };
    }

}
