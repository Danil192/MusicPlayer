package com.kefirstudio.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

public class ThemeManager {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String THEME_KEY = "theme";

    public static void setTheme(Context context, boolean isDarkTheme) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(THEME_KEY, isDarkTheme);
        editor.apply();

        // Применение новой темы
        applyTheme(context);
    }

    public static boolean isDarkTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(THEME_KEY, false);
    }

    public static void applyTheme(Context context) {
        boolean isDarkTheme = isDarkTheme(context);
        context.setTheme(isDarkTheme ? R.style.DarkTheme : R.style.LightTheme);
    }
}
