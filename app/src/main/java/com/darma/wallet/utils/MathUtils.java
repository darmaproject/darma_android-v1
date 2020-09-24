package com.darma.wallet.utils;

import java.math.BigDecimal;

/**
 * Created by Darma Project on 2019/12/23.
 */
public class MathUtils {


    public static String multiply(BigDecimal b1,BigDecimal b2){

        return b1.multiply(b2).setScale(8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();

    }


    public static String divide(BigDecimal b1,BigDecimal b2){

        return b1.divide(b2 ,8, BigDecimal.ROUND_HALF_UP).setScale(8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();

    }
}
