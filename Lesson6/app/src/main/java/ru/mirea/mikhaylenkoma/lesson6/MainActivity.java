package ru.mirea.mikhaylenkoma.lesson6;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.mikhaylenkoma.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static final String GROUP_KEY = "GROUP";
    public static final String NUM_IN_GROUP_KEY = "NUM_IN_GROUP";
    public static final String FAVOURITE_FILM_KEY = "FAV_FILM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPref = getSharedPreferences("settings_lesson6", Context.MODE_PRIVATE);
        binding.editTextGroup.setText(sharedPref.getString(GROUP_KEY, ""));
        binding.editTextNumInGroup.setText(sharedPref.getString(NUM_IN_GROUP_KEY, ""));
        binding.editTextFavFilm.setText(sharedPref.getString(FAVOURITE_FILM_KEY, ""));

        binding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString(GROUP_KEY, binding.editTextGroup.getText().toString());
                editor.putString(NUM_IN_GROUP_KEY, binding.editTextNumInGroup.getText().toString());
                editor.putString(FAVOURITE_FILM_KEY, binding.editTextFavFilm.getText().toString());
                editor.apply();
            }
        });

    }
}