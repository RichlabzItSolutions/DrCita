package com.drcita.user.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.drcita.user.CaafisomApp;

public class PreferenceUtil {
    static PreferenceUtil util;
    private final SharedPreferences mappr;

    public static PreferenceUtil getInstance() {
        if (util == null) util = new PreferenceUtil();
        return util;
    }

    private PreferenceUtil() {
        mappr = CaafisomApp.getContext().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void save(String key, Object value) {
        if (value instanceof String) {
            saveString(key, (String) value);
        } else if (value instanceof Boolean) {
            saveBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            saveInt(key, (int) value);
        } else if (value instanceof Long) {
            saveLong(key, (Long) value);
        }
    }

    private void saveLong(String key, Long value) {
        mappr.edit().putLong(key, value).apply();
    }

    private void saveInt(String key, int value) {
        mappr.edit().putInt(key, value).apply();
    }

    private void saveBoolean(String key, Boolean value) {
        mappr.edit().putBoolean(key, value).apply();
    }

    private void saveString(String key, String value) {
        mappr.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return mappr.getString(key, "");
    }

    public boolean get(String key) {
        return mappr.getBoolean(key, false);
    }

    public int getInt(String key) {
        return mappr.getInt(key, 0);
    }

    public Long getLong(String key) {
        return mappr.getLong(key, 0);
    }

    public void clear() {
        mappr.edit().clear().apply();
    }

    boolean contains(String key) {
        return mappr.contains(key);
    }
}
