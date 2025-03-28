package com.drcita.user;

import androidx.annotation.StringRes;

import java.util.HashMap;
import java.util.Map;

public enum LanguageEnum {
    English_India(1, R.string.en),
    Hindi_India(2, R.string.hi);
    private static final Map<Integer, LanguageEnum> map = new HashMap<>();

    static {
        for (LanguageEnum myEnum : values()) {
            map.put(myEnum.getValue(), myEnum);
        }
    }

    private final int value;
    private int name;

    LanguageEnum(int i, @StringRes int a) {
        value = i;
        this.name = a;
    }

    public int getValue() {
        return value;
    }

    public static LanguageEnum getByValue(int value) {
        return map.get(value);
    }

    public int getName() {
        return name;
    }
}
