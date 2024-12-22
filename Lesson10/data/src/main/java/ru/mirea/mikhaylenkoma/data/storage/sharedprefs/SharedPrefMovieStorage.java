package ru.mirea.mikhaylenkoma.data.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;

import ru.mirea.mikhaylenkoma.data.data.storage.models.Movie;
import ru.mirea.mikhaylenkoma.data.data.storage.MovieStorage;

public class SharedPrefMovieStorage implements MovieStorage {

    private static final String SHARED_PREFS_NAME = "films";
    private static final String KEY = "film";
    private static final String DATE_KEY = "movie_date";
    private static final String ID_KEY = "movie_id";

    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPrefMovieStorage(Context iContext) {
        context = iContext;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Movie get() {
        String movieName = sharedPreferences.getString(KEY, "Sobibor");
        String movieDate = sharedPreferences.getString(DATE_KEY, String.valueOf(LocalDate.now()));
        int movieId = sharedPreferences.getInt(ID_KEY, -1);

        return new Movie(movieId, movieName, movieDate);
    }

    @Override
    public boolean save(Movie movie) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY, movie.getName());
        editor.putString(DATE_KEY, String.valueOf(LocalDate.now()));
        editor.putInt(ID_KEY, 1);
        editor.apply();

        return false;
    }


}