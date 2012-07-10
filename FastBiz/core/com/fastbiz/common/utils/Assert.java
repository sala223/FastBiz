package com.fastbiz.common.utils;

public abstract class Assert extends org.springframework.util.Assert{

    public static void normalNumber(double number, String message){
        if (Double.isInfinite(number)) {
            throw new IllegalArgumentException(message);
        }
        if (Double.isNaN(number)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void normalNumber(double number){
        normalNumber(number, "[Assertion failed] - " + number + " is not a normal number");
    }

    public static void lessThan(double number, double compared, String message){
        normalNumber(number, message);
        if (number >= compared) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void lessThan(double number, double compared){
        lessThan(number, compared, "[Assertion failed] - " + number + " is not less than " + compared);
    }

    public static void lessOrEqualThan(double number, double compared, String message){
        normalNumber(number, message);
        if (number > compared) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void lessOrEqualThan(double number, double compared){
        lessThan(number, compared, "[Assertion failed] - " + number + " is not less or equal than " + compared);
    }

    public static void greaterThan(double number, double compared, String message){
        normalNumber(number, message);
        if (number <= compared) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void greaterThan(double number, double compared){
        greaterThan(number, compared, "[Assertion failed] - " + number + " is not greater than " + compared);
    }

    public static void greaterOrEqualThan(double number, double compared, String message){
        normalNumber(number, message);
        if (number < compared) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void greaterOrEqualThan(double number, double compared){
        greaterOrEqualThan(number, compared, "[Assertion failed] - " + number + " is not greater or equal than " + compared);
    }
}