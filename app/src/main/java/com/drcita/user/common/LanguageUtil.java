package com.drcita.user.common;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.drcita.user.CaafisomApp;
import com.drcita.user.LanguageEnum;

import java.util.Locale;

public class LanguageUtil {

    private static final String STRING = "string";

    public static void setLanguage(Context context, String language) {
        Log.e("TAG", "setLanguage: " + language);
        if(language==null){
            language = LanguageEnum.English_India.name();
        }
        if(language.equals("1")){
            language = LanguageEnum.English_India.name();
        } else if(language.equals("2")){
            language = LanguageEnum.Hindi_India.name();
        }
        // Save selected language
        persist(language);

        // Update language
        String[] lang = language.split("_");
        Locale locale = new Locale(lang[0], lang[1]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private static void persist(String language) {
        PreferenceUtil.getInstance().save(Constants.LANGUAGE1, language);
    }

    public static String currentLanguageString(Context context) {
        return getString(context, currentLanguage(context));
    }

    public static String getString(Context context, String identifier) {
        int resourceId = context.getResources().getIdentifier(identifier, STRING, context.getPackageName());
        return context.getResources().getString(resourceId);
    }

    public static String currentLanguage(Context context) {
        if (!PreferenceUtil.getInstance().contains(Constants.LANGUAGE1)) {
            setLanguage(context, CaafisomApp.getContext().getString(LanguageEnum.English_India.getName()));
        }
        return PreferenceUtil.getInstance().getString(Constants.LANGUAGE1);
    }
}
