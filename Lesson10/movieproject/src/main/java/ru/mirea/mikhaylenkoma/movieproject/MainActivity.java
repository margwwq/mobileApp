package ru.mirea.mikhaylenkoma.movieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.mirea.mikhaylenkoma.data.data.MovieRepositoryImpl;
import ru.mirea.mikhaylenkoma.data.data.storage.MovieStorage;
import ru.mirea.mikhaylenkoma.data.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.mikhaylenkoma.domain.domain.repository.MovieRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text = findViewById(R.id.editText);
        TextView textView = findViewById(R.id.textData);

        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean result = new
                        ru.mirea.mikhaylenkoma.domain.domain.usecases.SaveMovieToFavoriteUseCase(movieRepository).execute(new ru.mirea.mikhaylenkoma.domain.domain.models.Movie(2,
                        text.getText().toString()));
                textView.setText(String.format("Save result %s", result));
            }
        });

        findViewById(R.id.buttonShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ru.mirea.mikhaylenkoma.domain.domain.models.Movie moview = new ru.mirea.mikhaylenkoma.domain.domain.usecases.GetFavoriteFilmUseCase(movieRepository).execute();
                textView.setText(String.format("Save result %s", moview.getName()));
            }
        });
    }

}