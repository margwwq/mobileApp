package ru.mirea.mikhaylenkoma.movieproject.data;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.mikhaylenkoma.movieproject.domain.models.Movie;
import ru.mirea.mikhaylenkoma.movieproject.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private Context context;

    public MovieRepositoryImpl(Context context){
        this.context = context;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        SharedPreferences sharedPref = context.getSharedPreferences("films", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("film", movie.getName());
        editor.apply();

        return true;
    }

    @Override
    public Movie getMovie() {
        SharedPreferences sharedPref = context.getSharedPreferences("films", Context.MODE_PRIVATE);
        String filmName = sharedPref.getString("film", "Sobibor");

        return new Movie(1, filmName);
    }

}
