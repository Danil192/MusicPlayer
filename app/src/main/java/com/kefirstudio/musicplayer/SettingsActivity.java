package com.kefirstudio.musicplayer;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroupTheme;
    private RadioButton radioButtonLight;
    private RadioButton radioButtonDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioButtonLight = findViewById(R.id.radioButtonLight);
        radioButtonDark = findViewById(R.id.radioButtonDark);

        // Установка текущей темы
        boolean isDarkTheme = ThemeManager.isDarkTheme(this);
        if (isDarkTheme) {
            radioButtonDark.setChecked(true);
        } else {
            radioButtonLight.setChecked(true);
        }

        radioGroupTheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonLight) {
                ThemeManager.setTheme(this, false);
            } else if (checkedId == R.id.radioButtonDark) {
                ThemeManager.setTheme(this, true);
            }

            // Перезапуск Activity для применения новой темы
            recreate();
        });
    }
}
