package com.pransquare.nems.utils;

import java.text.DecimalFormat;

public class NumberToWord {

    private NumberToWord() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String[] tensNames = {
        "",
        "ten",
        "twenty",
        "thirty",
        "forty",
        "fifty",
        "sixty",
        "seventy",
        "eighty",
        "ninety"
    };

    private static final String[] numNames = {
        "",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
        "ten",
        "eleven",
        "twelve",
        "thirteen",
        "fourteen",
        "fifteen",
        "sixteen",
        "seventeen",
        "eighteen",
        "nineteen"
    };

    public static String convertLessThanOneThousand(int number) {
        if (number < 0 || number >= 1000) {
            throw new IllegalArgumentException("Number must be between 0 and 999");
        }

        String current;

        if (number % 100 < 20) {
            current = numNames[number % 100];
            number /= 100;
        } else {
            current = numNames[number % 10];
            number /= 10;

            current = tensNames[number % 10] + (current.isEmpty() ? "" : " " + current);
            number /= 10;
        }

        if (number == 0) return current.trim();
        return numNames[number] + " hundred" + (current.isEmpty() ? "" : " " + current);
    }

    public static String convert(long number) {
        if (number == 0) {
            return "zero";
        }

        if (number > 999999999) {
            throw new IllegalArgumentException("Number exceeds the 9-digit limit (max: 999,999,999)");
        }

        // Pad to 9 digits
        String mask = "000000000";
        DecimalFormat df = new DecimalFormat(mask);
        String snumber = df.format(number);

        int crores    = Integer.parseInt(snumber.substring(0, 2));
        int lakhs     = Integer.parseInt(snumber.substring(2, 4));
        int thousands = Integer.parseInt(snumber.substring(4, 6));
        int hundreds  = Integer.parseInt(snumber.substring(6, 9));

        StringBuilder result = new StringBuilder();

        if (crores > 0) {
            result.append(convertLessThanOneThousand(crores)).append(" crore ");
        }
        if (lakhs > 0) {
            result.append(convertLessThanOneThousand(lakhs)).append(" lakh ");
        }
        if (thousands > 0) {
            result.append(convertLessThanOneThousand(thousands)).append(" thousand ");
        }
        if (hundreds > 0) {
            result.append(convertLessThanOneThousand(hundreds));
        }

        return result.toString().trim();
    }

}
