package com.drcita.user.utitlities;



import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "DoctorFiltersPrefs";

    public static void saveSelectedFilters(Context context, String categoryKey, Set<String> selectedIds) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(categoryKey, selectedIds).apply();
    }

    public static Set<String> getSelectedFilters(Context context, String categoryKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getStringSet(categoryKey, new HashSet<>());
    }

    public static void clearFilters(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static void clearCategory(Context context, String categoryKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(categoryKey).apply();
    }
}
