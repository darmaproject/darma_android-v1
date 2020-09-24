//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wallet.model;

import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Coin implements  Comparable<Coin>, Serializable {
    public static final String COIN_UNIT = "DMC";

    public static final int SMALLEST_UNIT_EXPONENT = 8;
    private static final long COIN_VALUE = LongMath.pow(10L, SMALLEST_UNIT_EXPONENT);
    public static final Coin ZERO = valueOf(0L);
    public static final Coin COIN;
    public static final Coin NEGATIVE ;
    public final long value;
//    private static final MonetaryFormat FRIENDLY_FORMAT;
//    private static final MonetaryFormat PLAIN_FORMAT;

    private Coin(long satoshis) {
        this.value = satoshis;
    }

    public static Coin valueOf(long satoshis) {
        return new Coin(satoshis);
    }

    public int smallestUnitExponent() {
        return SMALLEST_UNIT_EXPONENT;
    }

    public long getValue() {
        return this.value;
    }


    public static Coin parseCoin(String str) {
        try {
            long satoshis = (new BigDecimal(str)).movePointRight(SMALLEST_UNIT_EXPONENT).toBigIntegerExact().longValue();
            return valueOf(satoshis);
        } catch (ArithmeticException var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    public Coin add(Coin value) {
        return new Coin(LongMath.checkedAdd(this.value, value.value));
    }

    public Coin plus(Coin value) {
        return this.add(value);
    }

    public Coin subtract(Coin value) {
        return new Coin(LongMath.checkedSubtract(this.value, value.value));
    }

    public Coin minus(Coin value) {
        return this.subtract(value);
    }

    public Coin multiply(long factor) {
        return new Coin(LongMath.checkedMultiply(this.value, factor));
    }

    public Coin times(long factor) {
        return this.multiply(factor);
    }

    public Coin times(int factor) {
        return this.multiply((long)factor);
    }

    public Coin divide(long divisor) {
        return new Coin(this.value / divisor);
    }

    public Coin div(long divisor) {
        return this.divide(divisor);
    }

    public Coin div(int divisor) {
        return this.divide((long)divisor);
    }

    public Coin[] divideAndRemainder(long divisor) {
        return new Coin[]{new Coin(this.value / divisor), new Coin(this.value % divisor)};
    }

    public Coin divide(Coin divisor) {
        return this.multiply(COIN_VALUE).divide( divisor.value) ;
    }

    public boolean isPositive() {
        return this.signum() == 1;
    }

    public boolean isNegative() {
        return this.signum() == -1;
    }

    public boolean isZero() {
        return this.signum() == 0;
    }

    public boolean isGreaterThan(Coin other) {
        return this.compareTo(other) > 0;
    }

    public boolean isLessThan(Coin other) {
        return this.compareTo(other) < 0;
    }

    public Coin shiftLeft(int n) {
        return new Coin(this.value << n);
    }

    public Coin shiftRight(int n) {
        return new Coin(this.value >> n);
    }

    public int signum() {
        if (this.value == 0L) {
            return 0;
        } else {
            return this.value < 0L ? -1 : 1;
        }
    }

    public Coin negate() {
        return new Coin(-this.value);
    }

    public long longValue() {
        return this.value;
    }


    public String toFriendlyString() {
        return toPlainString() +" "+COIN_UNIT;
    }

    public String toRateString() {

        return "";
//        Rate rate= WalletManager.getInstance().getCoinRate();
//        if(rate==null||rate.equals(Rate.NEGATIVE)){
//            return "";
//        }
//
//        return valueOf(rate.getRate().multiply(new BigDecimal(value)).longValue()).toLegalCurrencyString() +" "+rate.getCode();
    }
    public float toFloat() {
        if(longValue()==0){
            return 0;
        }
        return new BigDecimal(longValue()).movePointLeft(SMALLEST_UNIT_EXPONENT).setScale(8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().floatValue();

    }

    public String toPlainString() {
        if(longValue()==0){
            return "0.0";
        }
        return new BigDecimal(longValue()).movePointLeft(SMALLEST_UNIT_EXPONENT).setScale(8, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();

    }
    public String toLegalCurrencyString() {
        if(longValue()==0){
            return "0.0";
        }
        return new BigDecimal(longValue()).movePointLeft(SMALLEST_UNIT_EXPONENT).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();

    }
    public String toString() {
        return Long.toString(this.value);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            return this.value == ((Coin)o).value;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int)this.value;
    }

    public int compareTo(Coin other) {
        return Longs.compare(this.value, other.value);
    }

    static {
        COIN = valueOf(COIN_VALUE);
        NEGATIVE=valueOf(-1);
    }
}
