package com.pransquare.nems.utils;

import java.util.HashMap;
import java.util.Map;

public class CurrencySymbols {
    private static final Map<String, String> currencyList = new HashMap<>();

    static {
        currencyList.put("usd", "$");
        currencyList.put("eur", "€");
        currencyList.put("inr", "₹");
    }

    public static String getCurrencySymbol(String currencyCode) {
        return currencyList.getOrDefault(currencyCode.toLowerCase(), "Unknown Currency");
    }
}
